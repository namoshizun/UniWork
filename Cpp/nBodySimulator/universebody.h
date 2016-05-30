#ifndef UNIVERSEBODY_H
#define UNIVERSEBODY_H

#include "universecomponent.h"
#include <QVector2D>

class UniverseBody : public UniverseComponent
{
public:
    UniverseBody(
            UniverseComponentType type,
            const std::string& name,
            const std::string& parentName = "");

    virtual ~UniverseBody() {}

    /*********************************************
     * Methods newly added
     * *******************************************/
    void accept (Visitor* visitor) { visitor->visit(this); }

    /*********************************************
     * Inherited methods from UniverseComponent
     * *******************************************/
    //render the subtree
    virtual void render(QPainter &painter) const;
    virtual void renderLabel(QPainter& painter) const;

    //apply the attraction from this component, to the specified leaf
    virtual void addAttractionTo(UniverseBody& body) const;

    //update attractive forces to all components of this object, from the specified component
    virtual void addAttractionFrom(const UniverseComponent& component);

    //reset the accumulated forces to zero
    virtual void resetForces();

    //update the positions of all components of this object
    virtual void updatePosition(int timestep);

    //convert the initial (relative) position and velocity, to an absolute one
    //by translating the position and velocity with the values provided
    void convertRelativeToAbsolute(QVector2D p, QVector2D v) {
        m_position += p;
        m_velocity += v;
    }

    /*********************************************
     * Accessor methods
     * *******************************************/
    const QVector2D& getPosition() const { return m_position; }
    const QVector2D& getVelocity() const { return m_velocity; }
    const QColor& getColor() const { return m_color; }
    double getPositionX() const { return m_position.x(); }
    double getPositionY() const { return m_position.y(); }
    double getMass() const { return m_mass; }
    double getRadius() const { return m_radius; }

    void addForce(QVector2D f) { m_force += f; }
    void setPosition(const double x, const double y) { m_position.setX(x); m_position.setY(y); }
    void setVelocity(const double x, const double y) { m_velocity.setX(x); m_velocity.setY(y); }
    void setRadius(const double& radius) { m_radius = radius; }
    void setMass(const double& mass) { m_mass = mass; }
    void setColor(const QColor& color) { m_color = color; }

private:
    QVector2D m_force;
    QVector2D m_velocity;
    QVector2D m_position;

    double m_radius;
    double m_mass;
    QColor m_color;
};

#endif // UNIVERSEBODY_H
