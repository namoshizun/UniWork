#ifndef DIALOG_H
#define DIALOG_H

#include "ui_planetinfo.h"
#include "config.h"
#include "zodiac.h"
#include "universecomponent.h"
#include "universecomposite.h"
#include "visitor.h"
#include "adjustvisitor.h"
#include "inspectvisitor.h"
#include "memento.h"

#include <list>
#include <QDialog>
#include <QTimer>
#include <QKeyEvent>
#include <QMouseEvent>
#include <QWheelEvent>
#include <QMessageBox>

enum UNDOABLE_ACTION { SET_CENTER, TRACK_PLANET, ADJUST, INVALID};

namespace Ui {
class Dialog;
class PlanetInfo;
}

// Panel used to show planet info
class PlanetInfoDialog : public QDialog
{
    Q_OBJECT

public:
    explicit PlanetInfoDialog(QWidget *parent = 0)
        : QDialog(parent){}
};

class Dialog : public QDialog
{
    Q_OBJECT

public:
    explicit Dialog(QWidget *parent = 0);
    virtual ~Dialog();

private slots:
    void togglePause();
    void toggleZodiacs();
    void toggleLabels();
    void toggleAutoAdjust();

    void nextFrame();
    void undo();
    void replaySimulation();
    void submitViewChange();
    void adjustView();
    void lockBody(UniverseBody*);
    void unlockBody(UniverseBody*);

private:
    //pause (or unpause) the simulation
    void pause(bool pause);
    //Enable or disable auto-adjust
    void autoadjust(bool _auto);

    // EVENTS
    void paintEvent(QPaintEvent *event);
    void keyPressEvent(QKeyEvent *event);
    void keyReleaseEvent(QKeyEvent *event);
    void mouseReleaseEvent(QMouseEvent *event);
    void wheelEvent(QWheelEvent *event);

    // Snapshot/Override
    std::list<Memento*> history;
    void addToHistory(const std::string& action);
    Memento* createMemento(const std::string& action);
    void restoreFromMemento(const Memento* m);

    // show warn message
    void warn(const std::string&);
    UNDOABLE_ACTION getAction(const std::string&);

private:

    Ui::Dialog* ui;
    Ui::PlanetInfo* ui_planetInfo;
    long m_timestamp; //simulation time since simulation start
    QTimer* m_timer; //Timer object for triggering updates
    QTimer* m_timerAdjust; // Time object for triggering view window adjust

    // flags
    bool m_paused; //is the simulation paused?
    bool m_renderZodiacs; //should Zodiacs be rendered?
    bool m_renderLabels; //should labels be rendered?
    bool m_readyToRecenter;
    bool m_readyToAccel;
    bool m_adjusting;
    bool m_lockingPlanet;

    // used for auto-adjust center
    int margin = 100;

    ViewState s; // A LOT OF THIGS!
    std::list<Zodiac>* m_zodiacs; //Vector of zodiac lines
    UniverseComponent* m_universe; //a composite of systems...
    Config* m_config; //the singleton config instance
};
#endif // DIALOG_H
