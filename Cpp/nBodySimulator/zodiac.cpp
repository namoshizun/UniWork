#include "zodiac.h"
#include "config.h"
#include <utility>

Zodiac::Zodiac()
{
}

Zodiac::~Zodiac()
{
}

void Zodiac::render(QPainter &painter) const
{
    double distanceScale = Config::getInstance()->getDistanceScale();
    painter.setPen(Qt::white);
    painter.setBrush(QColor(Qt::white));
    for(auto pair : lines) {
        double x1 = pair.first->getPositionX() / distanceScale;
        double y1 = pair.first->getPositionY() / distanceScale;
        double x2 = pair.second->getPositionX() / distanceScale;
        double y2 = pair.second->getPositionY() / distanceScale;
        painter.drawLine(QLineF(x1, y1, x2, y2));
    }
}

void Zodiac::add(UniverseBody* from, UniverseBody* to)
{
    lines.push_back( std::make_pair(from, to) );
}
