#include "universecomposite.h"

UniverseComposite::UniverseComposite(
        UniverseComponentType type,
        const std::string& name,
        const std::string& parentName)
        : UniverseComponent(type, name, parentName)
        , m_velocity(0, 0)
        , m_position(0, 0)
{ }

UniverseComposite::~UniverseComposite()
{
    for(UniverseComponent* child : m_children)
        delete child;
}

//render the subtree
void UniverseComposite::render(QPainter& painter) const
{
    for(UniverseComponent* child : m_children)
        child->render(painter);
}

void UniverseComposite::renderLabel(QPainter& painter) const
{
    for(UniverseComponent* child : m_children)
        child->renderLabel(painter);
}


//apply the attraction from this component, to the specified leaf
void UniverseComposite::addAttractionTo(UniverseBody& other) const
{
    for(UniverseComponent* child : m_children)
        child->addAttractionTo(other);
}

void UniverseComposite::resetForces()
{
    for(UniverseComponent* child : m_children)
        child->resetForces();
}

//update attractive forces to all components of this object, from the specified component
void UniverseComposite::addAttractionFrom(const UniverseComponent& component)
{
    for(UniverseComponent* child : m_children)
        child->addAttractionFrom(component);
}

//update the positions of all components of this object
void UniverseComposite::updatePosition(int timestep)
{
    for(UniverseComponent* child : m_children)
        child->updatePosition(timestep);
}

void UniverseComposite::convertRelativeToAbsolute(QVector2D p, QVector2D v)
{
    p += m_position;
    v += m_velocity;

    for(UniverseComponent* child : m_children)
        child->convertRelativeToAbsolute(p, v);
}
