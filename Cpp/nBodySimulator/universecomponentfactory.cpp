#include "universecomponentfactory.h"
#include <QDebug>

using namespace std;

UniverseComponentFactory::UniverseComponentFactory()
{
}

UniverseComponentFactory::~UniverseComponentFactory()
{
}

UniverseComponentType UniverseComponentFactory::stringToUniverseComponentType(
        const string& type) const
{
    if(type == "planet")    return planet;
    if(type == "blackhole") return blackhole;
    if(type == "star")      return star;
    if(type == "solarsystem") return solarsystem;
    if(type == "galaxy")    return galaxy;
    if(type == "cluster")   return cluster;
    if(type == "universe")  return universe;
    throw invalid_argument(type + " is not a valid UniverseComponent type");
}

UniverseComponent* UniverseComponentFactory::createUniverseComponent(
        const unordered_map<string, string>& block) const
{
    string name;
    try {
        name = block.at("name");
    }
    catch(...) {
        throw invalid_argument("A block was missing the name field");
    }

    string parentName = "";
    if(block.count("parent") > 0) {
        parentName = block.at("parent");
    }

    //this key is guaranteed to exist in the block (it's the heading)
    UniverseComponentType type = stringToUniverseComponentType(block.at("category"));
    switch(type)
    {
    case planet:
    case blackhole:
    case star:
        return createLeaf(block, name, parentName, type);
    default:
        return createComposite(block, name, parentName, type);
    }
}

QColor UniverseComponentFactory::parseColor(const string& value) const
{
    QColor color(value.c_str());
    if(color.isValid()) return color;
    qDebug() << "Invalid color, should be like #FF8800, but was:" << value.c_str();
    return Qt::white;
}

double UniverseComponentFactory::getDouble(
        const unordered_map<string, string>& block,
        const string& key,
        const string& name,
        bool required) const
{
    //fetch the value
    if(block.count(key) < 1) {
        if(required) {
            throw out_of_range("Error building " + name + ": missing required value: " + key);
        }
        //key was not in the block, but is not required, use 0.0 as a default
        return 0.0;
    }

    //try to convert the value to a double
    bool ok;
    double value = QString::fromStdString(block.at(key)).toDouble(&ok);
    if(!ok) throw invalid_argument("Error building " + name + ": " + block.at(key) + " is not a double");

    //success
    return value;
}

UniverseComponent* UniverseComponentFactory::createLeaf(
        const unordered_map<string, string>& block,
        const string& name,
        const string& parentName,
        UniverseComponentType type) const
{
    //fetch various doubles from the block. Throw errors if they are missing or invalid.
    double position_x = getDouble(block, "position_x", name, true);
    double position_y = getDouble(block, "position_y", name, true);
    double velocity_x = getDouble(block, "velocity_x", name, true);
    double velocity_y = getDouble(block, "velocity_y", name, true);
    double radius = getDouble(block, "radius", name, true);
    double mass = getDouble(block, "mass", name, true);

    //semantic checks
    if(radius <= 0.0) throw invalid_argument("Error building " + name + ": radius invalid");
    if(mass <= 0.0)   throw invalid_argument("Error building " + name + ": mass invalid");

    //build the body:
    UniverseBody* component = new UniverseBody(type, name, parentName);
    component->setPosition(position_x, position_y);
    component->setVelocity(velocity_x, velocity_y);
    component->setRadius(radius);
    component->setMass(mass);

    //use the colour argument, only if it exists
    if(block.count("color") > 0) component->setColor(parseColor(block.at("color")));

    return component;
}


UniverseComponent* UniverseComponentFactory::createComposite(
        const unordered_map<string, string>& block,
        const string& name,
        const string& parentName,
        UniverseComponentType type) const
{
    //fetch various doubles from the block. Throw errors only if they are invalid
    double position_x = getDouble(block, "position_x", name, false);
    double position_y = getDouble(block, "position_y", name, false);
    double velocity_x = getDouble(block, "velocity_x", name, false);
    double velocity_y = getDouble(block, "velocity_y", name, false);

    //build the body:
    UniverseComposite* component = new UniverseComposite(type, name, parentName);
    component->setPosition(position_x, position_y);
    component->setVelocity(velocity_x, velocity_y);

    return component;
}
