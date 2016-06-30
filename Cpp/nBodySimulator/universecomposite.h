#ifndef UNIVERSECOMPOSITE_H
#define UNIVERSECOMPOSITE_H

#include "universecomponent.h"
#include <QVector2D>
#include <string>
#include <vector>


class UniverseComposite : public UniverseComponent {

public:
    UniverseComposite(
            UniverseComponentType type,
            const std::string& name,
            const std::string& parentName = "");
    virtual ~UniverseComposite();

    //build up the composition
    virtual void add(UniverseComponent* component) { m_children.push_back(component); }

    void accept (Visitor* visitor) { visitor->visit(this); }

    /*********************************************
     * Inherited methods from UniverseComponent
     * *******************************************/
    virtual void render(QPainter& painter) const;
    virtual void renderLabel(QPainter& painter) const;
    virtual void addAttractionTo(UniverseBody& other) const;
    virtual void resetForces();
    virtual void addAttractionFrom(const UniverseComponent& component);
    virtual void updatePosition(int timestep);

    /*********************************************
     * Methods used to enable construction of the universe with relative positions
     * *******************************************/
    void setPosition(const double x, const double y) { m_position.setX(x); m_position.setY(y); }
    void setVelocity(const double x, const double y) { m_velocity.setX(x); m_velocity.setY(y); }
    //propagates the position and velocity of each object down to it's children
    //this should only be called ONCE
    void convertRelativeToAbsolute(QVector2D p, QVector2D v);

    // Accessories
    std::vector<UniverseComponent*>& getChildren() { return m_children; }

private:
    std::vector<UniverseComponent*> m_children;

    //used only to enable construction of the universe with relative positions
    QVector2D m_velocity;
    QVector2D m_position;
};

#endif // UNIVERSECOMPOSITE_H
