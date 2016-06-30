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
    , m_timestamp(0)
    , m_paused(false)
    , m_renderZodiacs(true)
    , m_renderLabels(true)
    , m_readyToRecenter(false)
    , m_readyToAccel(false)
    , m_adjusting(false)
    , m_lockingPlanet(false)
    , m_width(1200)
    , m_height(800)
    , m_pLocked(nullptr)
    , m_config(Config::getInstance())
{
    m_config->read("config.txt");
    m_universe = m_config->parseUniverseBlocks();
    m_zodiacs = m_config->parseZodiacBlocks();
    m_universe->convertRelativeToAbsolute(QVector2D(0, 0),QVector2D(0, 0));

    // simulation display configurations
    m_distanceScaleVariance = m_config->getDistanceScale() * 0.1;
    m_radiusScaleVariance = m_config->getRadiusScale() * 0.1;
    m_logPointVariance = m_config->getLogPointRadius() * 0.1;
    m_stepSizeVariance = m_config->getPhysicsStepSize() * 0.1;
    offsetX = m_width / 2; // make the center be (0, 0)
    offsetY = m_height / 2;
    m_center.setX(offsetX);
    m_center.setY(offsetY);

    // set backdrop to sky-blue and make the window appear
    ui->setupUi(this);
    this->setStyleSheet("background-color: #82CAFF;");
    this->resize(m_width, m_height);
    this->update();

    //create and connect buttons
    connect(ui->btn_pause, SIGNAL(released()), this, SLOT(togglePause()));
    connect(ui->btn_zodiac, SIGNAL(released()), this, SLOT(toggleZodiacs()));
    connect(ui->btn_label, SIGNAL(released()), this, SLOT(toggleLabels()));
    connect(ui->btn_adjust, SIGNAL(released()), this, SLOT(toggleAutoAdjust()));
    connect(ui->btn_replay, SIGNAL(released()), this, SLOT(replaySimulation()));
    connect(ui->btn_submitViewChange, SIGNAL(released()), this, SLOT(submitViewChange()));
    connect(ui->btn_undoChange, SIGNAL(released()), this, SLOT(undo()));
    connect(ui->btn_2D3D, SIGNAL(released()), this, SLOT(toggle2D3D()));

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

// toggle - begin
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

void Dialog::toggle2D3D()
{
    // TODO
}

// toggle - finish

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
        offsetX += m_width/2 - x.toInt();
        offsetY += m_height/2 - y.toInt();
    }
}

void Dialog::replaySimulation()
{
}

void Dialog::undo()
{
    if (!history.empty()){
        if (m_adjusting)
            warn("Please disable auto-adjusting before undo");
        else {
            Memento* m = history.back();
            restoreFromMemento(m);
            history.pop_back();
        }
    } else {
        warn("No saved snapshots");
    }
}

// HANDLE TIMERS
void Dialog::autoadjust(bool _auto)
{
    if (_auto) {
        addToHistory("ADJUST");
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
    m_pLocked = p;
    m_lockingPlanet = true;
    ui->l_pLocked->setText(QString::fromStdString(p->getName()));
}

void Dialog::unlockBody(UniverseBody *p)
{
    if (m_pLocked != nullptr){
        if (m_pLocked == p) {
            m_pLocked = nullptr;
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
        m_readyToRecenter = true;
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
        m_readyToRecenter = false;
        return;
    default:
        return;
    }
}


void Dialog::mouseReleaseEvent(QMouseEvent *event)
{
    ui->input_xCenter->clearFocus();
    ui->input_yCenter->clearFocus();

    if (m_readyToRecenter) {
        addToHistory("SET_CENTER");
        m_center.setX(event->pos().x());
        m_center.setY(event->pos().y());
        offsetX += m_width/2 - m_center.x();
        offsetY += m_height/2 - m_center.y();
    }
    else {
        // inspect planet information
        Visitor* visitor = new InspectVisitor(event->pos().x() - offsetX, event->pos().y() - offsetY);
        m_universe->accept(visitor);// try to find a planet that is considered to be clicked on
        UniverseBody* p = dynamic_cast<InspectVisitor*>(visitor)->getTarget();

        if (p != NULL) {
            std::stringstream sstream;
            PlanetInfoDialog* pDialog = new PlanetInfoDialog(this);
            ui_planetInfo->setupUi(pDialog);
            connect(ui_planetInfo->btn_trace, &QAbstractButton::released, this, [=]{ lockBody(p); });
            connect(ui_planetInfo->btn_untrace, &QAbstractButton::released, this, [=]{ unlockBody(p); });

            ui_planetInfo->l_name->setText(QString::fromStdString(p->getName()));
            ui_planetInfo->l_color->setText(p->getColor().name());
            ui_planetInfo->l_mass->setText(QString::number(p->getMass()));
            ui_planetInfo->l_parent->setText(QString::fromStdString(p->getParentName()));
            ui_planetInfo->l_radius->setText(QString::number(p->getRadius()));
            sstream << "X = " << p->getPosition().x() << " Y = " << p->getPosition().y();
            ui_planetInfo->l_pos->setText(QString::fromStdString(sstream.str()));
            sstream.str(""); // clear string stream
            sstream << "X = " << p->getVelocity().x() << " Y = " << p->getVelocity().y();
            ui_planetInfo->l_veloc->setText(QString::fromStdString(sstream.str()));

            pDialog->exec();
            delete pDialog;
        }
        delete visitor;
    }
}

void Dialog::wheelEvent(QWheelEvent *event)
{
    QPoint degree = event->angleDelta() / 8;
    int sign = degree.y() > 0 ? -1 : 1; // -1 = forward, 1 = backward

    if (m_readyToAccel) {
        m_config->setStepSizeChange(-sign * m_stepSizeVariance);
    } else {
        m_config->setDistanceScaleChange(sign * m_distanceScaleVariance);
        m_config->setRadiusScaleChange(sign * m_radiusScaleVariance);
        m_config->setLogPointRadiusChange(sign * m_logPointVariance);
    }
}
/********** Timed Events **********/
void Dialog::adjustView()
{
    // form collects: <xsum, ysum, counter, min_x, min_y, max_x, max_y> (all are absolute values)
    Visitor* visitor = new AdjustVisitor();
    m_universe->accept(visitor);
    const Form& form = dynamic_cast<AdjustVisitor*>(visitor)->getForm();

    double w = std::abs(form.max_x - form.min_x);
    double h = std::abs(form.max_y - form.min_y);
    double ws = w / (m_width - margin*2); // proper width scale
    double hs = h / (m_height - margin*2); // proper height scale
    double scale = std::max(ws, hs); // the max distance scale is selected as new scale.

    // get the proportion of distance scale change and tune the scales of others
    double p = (scale - m_config->getBaseDistanceScale()) / m_config->getBaseDistanceScale();
    m_config->m_distanceScaleChange = m_config->getBaseDistanceScale() * p;
    m_config->m_radiusScaleChange = m_config->getBaseRadiusScale() * p;
    m_config->m_logPointRadiusChange = m_config->getBaseLogPointRadius() * p;

    offsetX = -form.min_x / scale + margin;
    offsetY = -form.min_y / scale + margin;

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
    painter.translate(offsetX, offsetY);
    int w = m_width;
    int h = m_height;
    double ox = offsetX;
    double oy = offsetY;

    if (m_renderZodiacs) { // zodiacs?
        for(auto zodiac : *m_zodiacs)
            zodiac.render(painter);
    }
    if (m_renderLabels)  // labels?
        m_universe->renderLabel(painter);
    if (m_lockingPlanet) { // locking on a planet?
        offsetX += w/2 - (m_pLocked->getPositionX() / m_config->getDistanceScale() + ox);
        offsetY += h/2 - (m_pLocked->getPositionY() / m_config->getDistanceScale() + oy);
    }

    m_universe->render(painter);
    painter.restore();

    // draw the center cross?
    if (m_readyToRecenter){
        painter.setPen(Qt::red);
        painter.drawLine(0, h/2, w, h/2);
        painter.drawLine(w/2, 0, w/2, h);
    }
    // update x_offet and y_offset labels
    ui->l_xoffset->setText(QString::number(ox));
    ui->l_yoffset->setText(QString::number(oy));
}

/********** Snapshot/Overwrite **********/
void Dialog::addToHistory(const std::string &action)
{
    ui->comboBox_history->addItem(QString::fromStdString(action));
    history.push_back(createMemento(action));
}

Memento* Dialog::createMemento(const std::string& action)
{
    State* s = new State();
    packState(s);
    return new Memento(*s, action);
}

void Dialog::restoreFromMemento(const Memento *m)
{
    const State& state = m->getState();
    switch (getAction(m->action)) {
    case SET_CENTER :
        offsetX = state.offsetX;
        offsetY = state.offsetY;
        break;
    case TRACK_PLANET:
        unlockBody(m_pLocked);
        offsetX = state.offsetX;
        offsetY = state.offsetY;
        m_pLocked = state.m_pLocked;
        break;
    case ADJUST:
        offsetX = state.offsetX;
        offsetY = state.offsetY;
        m_config->m_stepSizeChange = state.m_stepSizeChange;
        m_config->m_distanceScaleChange = state.m_distanceScaleChange;
        m_config->m_radiusScaleChange = state.m_radiusScaleChange;
        m_config->m_logPointRadiusChange = state.m_logPointRadiusChange;

        break;
    default:
        break;
    }
    ui->comboBox_history->removeItem(ui->comboBox_history->count() - 1);
}

/********** Helper Methods **********/
void Dialog::packState(State *s)
{
    s->m_width = m_width;
    s->m_height = m_height;
    s->m_center = m_center;
    s->offsetX = offsetX;
    s->offsetY = offsetY;
    s->m_stepSizeChange = m_config->m_stepSizeChange;
    s->m_distanceScaleChange = m_config->m_distanceScaleChange;
    s->m_radiusScaleChange = m_config->m_radiusScaleChange;
    s->m_logPointRadiusChange = m_config->m_logPointRadiusChange;
    s->m_pLocked = m_pLocked;
}

void Dialog::warn(const std::string& text)
{
    QMessageBox msgBox;
    msgBox.setText(QString::fromStdString(text));
    msgBox.exec();
}

UNDOABLE_ACTION Dialog::getAction(const std::string& s)
{
    if (s == "SET_CENTER") return SET_CENTER;
    if (s == "TRACK_PLANET") return TRACK_PLANET;
    if (s == "ADJUST") return ADJUST;
    return INVALID;
}
