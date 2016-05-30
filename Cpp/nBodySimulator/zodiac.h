#ifndef ZODIAC_H
#define ZODIAC_H

#include "universebody.h"
#include <QPainter>
#include <list>
#include <utility>

class Zodiac
{
public:
    Zodiac();
    virtual ~Zodiac();

    virtual void render(QPainter &painter) const;

    //add a line to the zodiac, between the given bodies
    virtual void add(UniverseBody* from, UniverseBody* to);

private:
    //list of stored lines
    std::list<std::pair<UniverseBody*, UniverseBody*> > lines;

};

#endif // ZODIAC_H
