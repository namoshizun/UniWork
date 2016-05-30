#include "adjustvisitor.h"
#include "universebody.h"

AdjustVisitor::AdjustVisitor()
{
}

AdjustVisitor::~AdjustVisitor()
{
}

void AdjustVisitor::visit(UniverseBody *body)
{
    ++form.population;
    double x = body->getPositionX();
    double y = body->getPositionY();
    form.xsum += x;
    form.ysum += y;
    if (x < form.min_x) form.min_x = x;
    if (x < form.min_y) form.min_y = y;
    if (x > form.max_x) form.max_x = x;
    if (x > form.max_y) form.max_y = y;
}
