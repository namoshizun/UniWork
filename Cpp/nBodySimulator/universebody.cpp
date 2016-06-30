#include "universebody.h"
#include "config.h"
#include <QDebug>
#include <QGradient>

UniverseBody::UniverseBody(
        UniverseComponentType type,
        const std::string& name,
        const std::string& parentName)
        : UniverseComponent(type, name, parentName)
        , m_force(0, 0)
        , m_velocity(0, 0)
        , m_position(0, 0)
        , m_radius(1.0)
        , m_mass(1.0)
        , m_color(Qt::white)
{ }

void UniverseBody::render(QPainter& painter) const
{
    Config* config = Config::getInstance();

    //get scaled position and radius
    double x = m_position.x() / config->getDistanceScale();
    double y = m_position.y() / config->getDistanceScale();
    double radius = m_radius / config->getRadiusScale();

    if(config->getUseLogRadius())
        radius = std::log(m_radius / config->getLogPointRadius());

    if(radius < 1) {
        painter.setPen(m_color);
        painter.drawPoint(x, y);
    }
    else {

        //no outline
        painter.setPen(Qt::NoPen);

        //gradient brush
        QRadialGradient gradient(x, y, radius);
        gradient.setColorAt(0.25, m_color);
        gradient.setColorAt(1, Qt::transparent);

        painter.setBrush(gradient);

        painter.drawEllipse(QPointF(x, y), radius, radius);
    }
}

void UniverseBody::renderLabel(QPainter& painter) const
{
    Config* config = Config::getInstance();

    //get scaled position
    double x = m_position.x() / config->getDistanceScale();
    double y = m_position.y() / config->getDistanceScale();
    //draw the label
    painter.setPen(m_color);
    painter.drawText(QRectF(x, y, 150.0, 50.0), getName().c_str());
}


void UniverseBody::addAttractionTo(UniverseBody &other) const
{
    if(this == &other)  return;

    double other_mass = other.getMass();
    double dx = m_position.x() - other.getPositionX();
    double dy = m_position.y() - other.getPositionY();
    double distance = m_position.distanceToPoint(other.getPosition());

    if(distance == 0.0) return;

    double force = GRAVITATIONAL_CONSTANT * m_mass * other_mass / (distance * distance);

    //normalise the vector {dx, dy} by dividing it by the distance, to get the direction
    other.addForce(QVector2D(force * (dx / distance), force * (dy / distance)));
}

void UniverseBody::addAttractionFrom(const UniverseComponent &component)
{
    component.addAttractionTo(*this);
}

void UniverseBody::resetForces()
{

    m_force.setX(0.0);
    m_force.setY(0.0);
}

void UniverseBody::updatePosition(int timestep)
{
    // calculate acceleration
    QVector2D accel = m_force / m_mass;

    // remember the old velocity
    QVector2D oldVeloc(m_velocity);

    // calculate the new velocity (integrate acceleration)
    m_velocity += accel * timestep;

    // calculate the new position (integrate velocity)
    // slightly improved by using the average velocity during this timestep
    m_position += (m_velocity + oldVeloc)/2 * timestep;
}

