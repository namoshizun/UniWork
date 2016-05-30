#include "dialog.h"
#include "ui_dialog.h"

#include <tuple>
#include <limits>
#include <sstream>
#include <QKeyEvent>
#include <QPainter>
#include <QPushButton>
#include <QTimer>
#include <QDebug>

Dialog::Dialog(QWidget *parent)
    : QDialog(parent)
    , ui(new Ui::Dialog)
    , ui_planetInfo(new Ui::PlanetInfo)
    , m_paused(false)
    , m_renderZodiacs(true)
    , m_renderLabels(true)
    , m_adjusting(false)
    , m_timestamp(0)
    , m_config(Config::getInstance())
    // controllable at runtime
    , m_readyToAccel(false)
    , m_readyToLocate(false)
    , m_lockingPlanet(false)
{
    m_config->read("config.txt");
    m_universe = m_config->parseUniverseBlocks();
    m_zodiacs = m_config->parseZodiacBlocks();
    m_universe->convertRelativeToAbsolute(QVector2D(0, 0),QVector2D(0, 0));

    // controllable at runtime
    s.m_distanceScaleVariance = m_config->getDistanceScale() * 0.1;
    s.m_radiusScaleVariance = m_config->getRadiusScale() * 0.1;
    s.m_logPointVariance = m_config->getLogPointRadius() * 0.1;
    s.m_stepSizeVariance = m_config->getPhysicsStepSize() * 0.15;
    s.offsetX = s.m_width / 2;
    s.offsetY = s.m_height / 2;
    s.m_center.setX(s.offsetX);
    s.m_center.setY(s.offsetY);

    // set backdrop to sky-blue and make the window appear
    ui->setupUi(this);
    this->setStyleSheet("background-color: #82CAFF;");
    this->resize(s.m_width, s.m_height);
    this->update();

    //create and connect buttons
    connect(ui->btn_pause, SIGNAL(released()), this, SLOT(togglePause()));
    connect(ui->btn_zodiac, SIGNAL(released()), this, SLOT(toggleZodiacs()));
    connect(ui->btn_label, SIGNAL(released()), this, SLOT(toggleLabels()));
    connect(ui->btn_adjust, SIGNAL(released()), this, SLOT(toggleAutoAdjust()));
    connect(ui->btn_replay, SIGNAL(released()), this, SLOT(replaySimulation()));
    connect(ui->btn_submitViewChange, SIGNAL(released()), this, SLOT(submitViewChange()));
    connect(ui->btn_undoChange, SIGNAL(released()), this, SLOT(undo()));
    //setup timer
    m_timer = new QTimer(this);
    m_timerAdjust = new QTimer(this);
    connect(m_timer, SIGNAL(timeout()), this, SLOT(nextFrame()));
    connect(m_timerAdjust, SIGNAL(timeout()), this, SLOT(adjustView()));
    pause(false);
    autoadjust(false);
}

Dialog::~Dialog()
{
    delete ui;
    delete m_timer;
    delete m_universe;
    delete m_zodiacs;
    for (std::list<Memento*>::const_iterator it = history.begin(); it != history.end(); ++it)
        delete *it;
}

void Dialog::toggleZodiacs()
{
    m_renderZodiacs = !m_renderZodiacs;
}

void Dialog::toggleLabels()
{
    m_renderLabels = !m_renderLabels;
}

void Dialog::togglePause()
{
    pause(!m_paused);
}

void Dialog::toggleAutoAdjust()
{
    autoadjust(!m_adjusting);
}

void Dialog::submitViewChange()
{

    QString x = ui->input_xCenter->text();
    QString y = ui->input_yCenter->text();

    QRegExp re("-?\\d*");
    if (x.isEmpty() || y.isEmpty()) {
        warn("Must specifiy x and y coordinates of new center");
    } else if (!re.exactMatch(x) || !re.exactMatch(y)) {
        warn("Coordinate values must be integers");
    } else {
        // save a snapshot
        addToHistory("SET_CENTER");
        s.offsetX += s.m_width/2 - x.toInt();
        s.offsetY += s.m_height/2 - y.toInt();
    }
}

void Dialog::replaySimulation()
{
}

void Dialog::undo()
{

    if (!history.empty()){
        Memento* m = history.back();
        restoreFromMemento(m);
        history.pop_back();
    } else {
        warn("No saved snapshots");
    }
}

// HANDLE TIMERS
void Dialog::autoadjust(bool _auto)
{
    if (_auto) {
        m_timerAdjust->start(30); // adjust view every 30ms
        m_adjusting = true;
        ui->btn_adjust->setText(QString::fromStdString("Adjusting"));
    } else {
        m_timerAdjust->stop();
        m_adjusting = false;
        ui->btn_adjust->setText(QString::fromStdString("Adjust"));
    }
}

void Dialog::pause(bool pause)
{
    if(pause) {
        m_timer->stop();
        m_paused = true;
        ui->btn_pause->setText(QString::fromStdString("Play"));
    }
    else {
        m_timer->start(1000 / m_config->getFramesPerSecond());
        m_paused = false;
        ui->btn_pause->setText(QString::fromStdString("Pause"));
    }
}

void Dialog::lockBody(UniverseBody * p)
{
    addToHistory("TRACK_PLANET");
    s.m_p = p;
    m_lockingPlanet = true;
    ui->l_pLocked->setText(QString::fromStdString(p->getName()));
}

void Dialog::unlockBody(UniverseBody *p)
{
    if (s.m_p != nullptr){
        if (s.m_p == p) {
            s.m_p = nullptr;
            m_lockingPlanet = false;
            ui->l_pLocked->setText(QString::fromStdString("NONE"));
        }
    }
}

// ************* EVENTS *************
void Dialog::keyPressEvent(QKeyEvent *event)
{
    switch(event->key()) {
    case Qt::Key_Space:
        pause(!m_paused);
        return;
    case Qt::Key_A:
        m_readyToAccel = true;
        return;

    case Qt::Key_L:
        m_readyToLocate = true;
        return;
    default:
        return;
    }
}

void Dialog::keyReleaseEvent(QKeyEvent *event)
{
    switch(event->key()) {
    case Qt::Key_A:
        m_readyToAccel = false;
        return;
    case Qt::Key_L:
        m_readyToLocate = false;
        return;
    default:
        return;
    }
}


void Dialog::mouseReleaseEvent(QMouseEvent *event)
{
    ui->input_xCenter->clearFocus();
    ui->input_yCenter->clearFocus();
    if (m_readyToLocate) {
        s.m_center.setX(event->pos().x());
        s.m_center.setY(event->pos().y());
        qDebug() << "df_x" << s.m_width/2 - s.m_center.x();
        s.offsetX += s.m_width/2 - s.m_center.x();
        s.offsetY += s.m_height/2 - s.m_center.y();
    }
    else {
        Visitor* visitor = new CensusVisitor(event->pos().x() - s.offsetX, event->pos().y() - s.offsetY);
        m_universe->accept(visitor);
        UniverseBody* p = dynamic_cast<CensusVisitor*>(visitor)->getTarget();

        if (p != NULL) {
            std::stringstream ss;
            PlanetInfoDialog* pDialog = new PlanetInfoDialog(this);
            ui_planetInfo->setupUi(pDialog);
            connect(ui_planetInfo->btn_trace, &QAbstractButton::released, this, [=]{ lockBody(p); });
            connect(ui_planetInfo->btn_untrace, &QAbstractButton::released, this, [=]{ unlockBody(p); });

            ui_planetInfo->l_name->setText(QString::fromStdString(p->getName()));
            ui_planetInfo->l_color->setText(p->getColor().name());
            ui_planetInfo->l_mass->setText(QString::number(p->getMass()));
            ui_planetInfo->l_parent->setText(QString::fromStdString(p->getParentName()));
            ui_planetInfo->l_radius->setText(QString::number(p->getRadius()));
            ss << "X = " << p->getPosition().x() << " Y = " << p->getPosition().y();
            ui_planetInfo->l_pos->setText(QString::fromStdString(ss.str()));
            ss.str("");
            ss << "X = " << p->getVelocity().x() << " Y = " << p->getVelocity().y();
            ui_planetInfo->l_veloc->setText(QString::fromStdString(ss.str()));

            pDialog->exec();
            delete pDialog;
        }
        delete visitor;
    }
}

void Dialog::wheelEvent(QWheelEvent *event)
{
    QPoint numDegrees = event->angleDelta() / 8;
    if (numDegrees.y() > 0) { // forward
        if (m_readyToAccel)
            m_config->setStepSizeChange(s.m_stepSizeVariance);
        else {
            m_config->setDistanceScaleChange(-s.m_distanceScaleVariance);
            m_config->setRadiusScaleChange(-s.m_radiusScaleVariance);
            m_config->setLogPointRadiusChange(-s.m_logPointVariance);
        }
    } else {
        if (m_readyToAccel)
            m_config->setStepSizeChange(-s.m_stepSizeVariance);
        else {
            m_config->setDistanceScaleChange(s.m_distanceScaleVariance);
            m_config->setRadiusScaleChange(s.m_radiusScaleVariance);
            m_config->setLogPointRadiusChange(s.m_logPointVariance);
        }
    }
}
/********** Timed Events **********/
void Dialog::adjustView()
{
    // 7-tuple form: <xsum, ysum, counter, min_x, min_y, max_x, max_y> (absolute values)
    Visitor* visitor = new AdjustVisitor();
    m_universe->accept(visitor);
    const Form& form = dynamic_cast<AdjustVisitor*>(visitor)->getForm();

    double w = std::abs(form.max_x - form.min_x);
    double h = std::abs(form.max_y - form.min_y);
    double ws = w / (s.m_width - margin*2); // proper width scale
    double hs = h / (s.m_height - margin*2); // proper height scale
    double scale = std::max(ws, hs); // the max distance scale is selected as new scale.

    // get the proportion of distance scale change and tune the scales of others
    double p = (scale - m_config->getBaseDistanceScale()) / m_config->getBaseDistanceScale();
    m_config->m_distanceScaleChange = m_config->getBaseDistanceScale() * p;
    m_config->m_radiusScaleChange = m_config->getBaseRadiusScale() * p;
    m_config->m_logPointRadiusChange = m_config->getBaseLogPointRadius() * p;

    // get the new center <- useless though...
    double xavg = (form.xsum / form.population) / scale;
    double yavg = (form.ysum / form.population) / scale;

    s.offsetX = -form.min_x / scale + margin;
    s.offsetY = -form.min_y / scale + margin;

    delete visitor;
}

void Dialog::nextFrame()
{
    //reset the forces stored in every object to 0
    m_universe->resetForces();

    //update the forces acting on every object in the universe,
    //from every object in the universe
    m_universe->addAttractionFrom(*m_universe);

    //update the velocity and position of every object in the universe
    int step = m_config->getPhysicsStepSize() / m_config->getOvercalculatePhysicsAmount();
    for(int i = 0; i < m_config->getPhysicsStepSize(); i += step) {
        //update physics
        m_universe->updatePosition(step);
        //some time has passed
        m_timestamp += step;

        //update the window (this will trigger paintEvent)
        update();
    }
}

void Dialog::paintEvent(QPaintEvent *event)
{
    //suppress 'unused variable' warning
    Q_UNUSED(event);

    //redraw the universe
    QPainter painter(this);

    //offset the painter, save coordinate system beforehand
    painter.save();
    painter.translate(s.offsetX, s.offsetY);
    int w = s.m_width;
    int h = s.m_height;
    double ox = s.offsetX;
    double oy = s.offsetY;

    if (m_renderZodiacs) { // zodiacs?
        for(auto zodiac : *m_zodiacs)
            zodiac.render(painter);
    }
    if (m_renderLabels)  // labels?
        m_universe->renderLabel(painter);
    if (m_lockingPlanet) { // lock the center of view on a planet?
        s.offsetX += w/2 - (s.m_p->getPositionX() / m_config->getDistanceScale() + ox);
        s.offsetY += h/2 - (s.m_p->getPositionY() / m_config->getDistanceScale() + oy);
    }

    m_universe->render(painter);
    painter.restore();

    // draw the center cross
    painter.setPen(Qt::red);
    if (m_readyToLocate){
        painter.drawLine(0, h/2, w, h/2);
        painter.drawLine(w/2, 0, w/2, h);
    }
    // refresh label for x_offet and y_offset
    ui->l_xoffset->setText(QString::number(ox));
    ui->l_yoffset->setText(QString::number(oy));
}

/********** Snapshot and Override **********/

void Dialog::addToHistory(const std::string &action)
{
    ui->comboBox_history->addItem(QString::fromStdString(action));
    history.push_back(createMemento(action));
}

Memento* Dialog::createMemento(const std::string& action)
{

    return new Memento(s, action);
}

void Dialog::restoreFromMemento(const Memento *m)
{
    const ViewState& state = m->getState();
    switch (getAction(m->action)) {
    case SET_CENTER :
        s.offsetX = state.offsetX;
        s.offsetY = state.offsetY;
        break;
    case TRACK_PLANET:
        unlockBody(s.m_p);
        s.offsetX = state.offsetX;
        s.offsetY = state.offsetY;
        s.m_p = state.m_p;
        break;
    case ZOOM_IN:
    case ZOOM_OUT:
        s.m_distanceScaleVariance = state.m_distanceScaleVariance;
        s.m_logPointVariance = state.m_logPointVariance;
        s.m_radiusScaleVariance = state.m_radiusScaleVariance;
        break;
    case ADJUST:
        s.offsetX = state.offsetX;
        s.offsetY = state.offsetY;
        s.m_distanceScaleVariance = state.m_distanceScaleVariance;
        s.m_logPointVariance = state.m_logPointVariance;
        s.m_radiusScaleVariance = state.m_radiusScaleVariance;
        break;
    case ACCELERATE:
    case DECELERATE:
        break;
    default:
        break;
    }
    ui->comboBox_history->removeItem(ui->comboBox_history->count() - 1);
}

/********** Helper Methods **********/
void Dialog::warn(const std::string& text)
{
    QMessageBox msgBox;
    msgBox.setText(QString::fromStdString(text));
    msgBox.exec();
}

ACTION Dialog::getAction(const std::string& s)
{
    if (s == "SET_CENTER") return SET_CENTER;
    if (s == "TRACK_PLANET") return TRACK_PLANET;
    if (s == "ZOOM_IN") return ZOOM_IN;
    if (s == "ZOOM_OUT") return ZOOM_OUT;
    if (s == "ACCELERATE") return ACCELERATE;
    if (s == "DECELERATE") return DECELERATE;
    if (s == "ADJUST") return ADJUST;
    return INVALID;
}
