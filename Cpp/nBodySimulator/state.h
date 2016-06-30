#ifndef DIALOGSTATE_H
#define DIALOGSTATE_H
#include "universecomponent.h"
#include "dialog.h"

class State
{
public:
    State()
    {}

    State(State& s)
    {   // deep copy the passed state
        m_width = s.m_width;
        m_height = s.m_height;
        m_stepSizeChange = s.m_stepSizeChange;
        m_distanceScaleChange = s.m_distanceScaleChange;
        m_radiusScaleChange = s.m_radiusScaleChange;
        m_logPointRadiusChange = s.m_logPointRadiusChange;
        m_center = s.m_center;
        offsetX = s.offsetX;
        offsetY = s.offsetY;
        m_pLocked = s.m_pLocked;
    }

private:
    friend class Dialog;
    // windows size
    int m_width;
    int m_height;
    // rendering option changes
    double m_stepSizeChange;
    double m_distanceScaleChange;
    double m_radiusScaleChange;
    double m_logPointRadiusChange;
    // center of view
    QPoint m_center;
    double offsetX;
    double offsetY;
    // locked planet
    UniverseBody* m_pLocked;
};

#endif // DIALOGSTATE_H
