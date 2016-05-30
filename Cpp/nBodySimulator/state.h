#ifndef DIALOGSTATE_H
#define DIALOGSTATE_H
#include "universecomponent.h"
#include "dialog.h"

class State
{
public:
    State() {}
    virtual ~State() {}
    // TODO: override ostream operator
private:
    long m_timestamp;
};

class ViewState : public State
{
public:
    ViewState():
        State()
      , m_width(1200)
      , m_height(800)
    {}

    ViewState(ViewState& s)
    {
        m_width = s.m_width;
        m_height = s.m_height;
        m_distanceScaleVariance = s.m_distanceScaleVariance;
        m_radiusScaleVariance = s.m_radiusScaleVariance;
        m_logPointVariance = s.m_logPointVariance;
        m_stepSizeVariance = s.m_stepSizeVariance;
        m_center = s.m_center;
        offsetX = s.offsetX;
        offsetY = s.offsetY;
        m_p = s.m_p;
    }

private:
    friend class Dialog;
    // windows size
    int m_width;
    int m_height;
    // scale variances
    double m_distanceScaleVariance;
    double m_radiusScaleVariance;
    double m_logPointVariance;
    double m_stepSizeVariance;
    // center of view
    QPoint m_center;
    double offsetX;
    double offsetY;
    // locked planet
    UniverseBody* m_p;
};

class SimulationState : public State
{
public:
    SimulationState() {} // TODO
    ~SimulationState() {}

private:
    friend class Dialog;
    // flags
    bool m_paused; //is the simulation paused?
    bool m_renderZodiacs; //should Zodiacs be rendered?
    bool m_renderLabels; //should labels be rendered?
    bool m_readyToLocate;
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

#endif // DIALOGSTATE_H
