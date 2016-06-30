#include "inspectvisitor.h"
#include "universebody.h"

InspectVisitor::InspectVisitor(int x, int y) :
    Visitor()
  , xpos(x)
  , ypos(y)
  , target(NULL)
{
}

InspectVisitor::~InspectVisitor()
{
}

void InspectVisitor::visit(UniverseBody *body)
{
    Config* config = Config::getInstance();
    if (std::abs(body->getPositionX() / config->getDistanceScale()
        + body->getPositionY() / config->getDistanceScale()
        - xpos - ypos) < 6)
        target = body;
}
