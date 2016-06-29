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

#endif // DIALOGSTATE_H
