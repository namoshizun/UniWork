#include "config.h"
#include "universecomposite.h"
#include "universecomponentfactory.h"
#include <QDebug>
#include <fstream>
#include <iostream>
#include <unordered_map>
#include <utility>
#include <sstream>


using namespace std;

//Singleton variables
bool Config::s_initialised = false;
Config* Config::s_instance = nullptr;


//a type to make it easier to keep track of what the parser is doing
enum BlockType { none, settings, component, zodiac };

Config::Config() // Initialize to default values first, but can be updated to info given in config later
    : m_framesPerSecond(60)
    , m_physicsStepSize(40320)
    , m_overcalculatePhysics(1)
    , m_distanceScale(1.0e10)
    , m_radiusScale(1.0e7)
    , m_useLogRadius(true)
    , m_logPointRadius(1.0e6)
    , m_stepSizeChange(0)
    , m_distanceScaleChange(0)
    , m_radiusScaleChange(0)
    , m_logPointRadiusChange(0)
{
}

Config::~Config()
{
    //assigning responsibility for deleting the universe to the caller (Dialog)
    //using shared_pointer would be a much smarter solution
}

Config* Config::getInstance()
{
    if(!s_initialised)
    {
        s_instance = new Config();
        s_initialised = true;
    }
    return s_instance;
}

/***** Text content processing/checking *****/
string Config::stripComment(const string& line) const
{
    if(line.length() == 0)
    {
        return line;
    }
    int index = line.find_first_of('/');
    return line.substr(0, index);
}

bool Config::isBlockHeading(const string& line) const
{
    return (line.length() > 0)
            && (line[0] == '[')
            && (line[line.length()-1] == ']');
}

bool Config::isKeyValuePair(const string& key, const string& op, const string& value) const
{
    return (key.length() > 0) && (op == "=") && (value.length() > 0);
}

//acquire file, read strings, close file
void Config::readLines(const string& path, list<string>& output) const
{
    ifstream configfile(path.c_str());
    if(!configfile.is_open()) {
        qDebug() << "Unable to open file: " << path.c_str();
    }
    string line;
    while(getline(configfile, line))
    {
        output.push_back(line);
    }
    configfile.close();
}

void Config::read(const string& path)
{
    m_settings.clear();
    m_universeBlocks.clear();
    m_zodiacBlocks.clear();

    list<string> lines;
    unordered_map<string, string> block;
    list<pair<string, string> > zodiacBlock;

    BlockType currentBlockType = none;

    readLines(path, lines);
    int lineNumber = 0; // for debugging purpose

    for(string line : lines)
    {
        ++lineNumber;

        //try to read three strings (breaks on spaces!)
        stringstream ss(stripComment(line));
        string key, op, value;
        ss >> key >> op >> value;

        //skip blank lines silently
        if(key.length() == 0) continue;

        //starting a new heading
        if(isBlockHeading(key)) {
            //save the finished block
            switch(currentBlockType) {
            case component:
                m_universeBlocks.emplace_back(block);
                block.clear();
                break;
            case zodiac:
                m_zodiacBlocks.emplace_back(zodiacBlock);
                zodiacBlock.clear();
                break;
            case settings:
            case none:
                break;
            }

            //start the new block
            string label = key.substr(1, key.length()-2);
            if(label == "zodiac") {
                currentBlockType = zodiac;
            } else if(label == "settings") {
                currentBlockType = settings;
            } else {
                currentBlockType = component;
                block.clear();
                block.insert(make_pair("category", label)); // category : planet/cluster/galaxy/blackhole....
            }
        }
        //read a k-v pair within a heading
        else if(isKeyValuePair(key, op, value)) {
            switch(currentBlockType) {
            case settings:
                m_settings.insert(make_pair(key, value));
                break;
            case component:
                block.insert(make_pair(key, value));
                break;
            case zodiac:
                zodiacBlock.emplace_back(make_pair(key, value));
                break;
            case none:
            default:
                qDebug() << "ERROR on line" << lineNumber << ", file should have started with a [heading]";
            }
        }
        else {
            qDebug() << "ERROR on line" << lineNumber
                     << "Expected: 'key = value' or '[block]'"
                     << "actually:"
                     << line.c_str();
        }
    }

    //end of file, save the finished block
    switch(currentBlockType) {
    case component:
        m_universeBlocks.emplace_back(block);
        block.clear();
        break;
    case zodiac:
        m_zodiacBlocks.emplace_back(zodiacBlock);
        zodiacBlock.clear();
        break;
    case settings:
    case none:
        break;
    }

    //update the settings
    parseSettings();
}

void Config::parseSettings()
{
    if (m_settings.count("FRAMES_PER_SECOND") > 0) {
        stringstream ss(m_settings.at("FRAMES_PER_SECOND"));
        ss >> m_framesPerSecond;
        qDebug() << "FRAMES_PER_SECOND" << m_framesPerSecond;
    }

    if (m_settings.count("PHYSICS_STEP_SIZE") > 0) {
        stringstream ss(m_settings.at("PHYSICS_STEP_SIZE"));
        ss >> m_physicsStepSize;
        qDebug() << "PHYSICS_STEP_SIZE" << m_physicsStepSize;
    }

    if (m_settings.count("OVERCALCULATE_PHYSICS") > 0) {
        stringstream ss(m_settings.at("OVERCALCULATE_PHYSICS"));
        ss >> m_overcalculatePhysics;
        if(m_overcalculatePhysics < 1)
            m_overcalculatePhysics = 1;
        qDebug() << "OVERCALCULATE_PHYSICS" << m_overcalculatePhysics;
    }

    if (m_settings.count("DISTANCE_SCALE") > 0) {
        stringstream ss(m_settings.at("DISTANCE_SCALE"));
        ss >> m_distanceScale;
        qDebug() << "DISTANCE_SCALE" << m_distanceScale;
    }

    if (m_settings.count("RADIUS_SCALE") > 0) {
        stringstream ss(m_settings.at("RADIUS_SCALE"));
        ss >> m_radiusScale;
        qDebug() << "RADIUS_SCALE" << m_radiusScale;
    }

    if (m_settings.count("USE_LOG_RADIUS") > 0) {
        stringstream ss(m_settings.at("USE_LOG_RADIUS"));
        ss >> m_useLogRadius;
        qDebug() << "USE_LOG_RADIUS" << m_useLogRadius;
    }

    if (m_settings.count("LOG_POINT") > 0) {
        stringstream ss(m_settings.at("LOG_POINT"));
        ss >> m_logPointRadius;
        qDebug() << "LOG_POINT" << m_logPointRadius;
    }
}

void Config::joinComponents(UniverseComponent* parent, UniverseComponent* child)
{
    switch(parent->getType()) {
    //allow anything to join to the universe
    case universe:
        dynamic_cast<UniverseComposite*>(parent)->add(child);
        return;

    //allow clusters and galaxies to join to a cluster
    case cluster:
        switch(child->getType()) {
        case cluster:
        case galaxy:
            dynamic_cast<UniverseComposite*>(parent)->add(child);
            return;
        default:
            break;
        }
        break;

    //allow solarsystems and blackholes to join to a galaxy
    case galaxy:
        switch(child->getType()) {
        case solarsystem:
        case blackhole:
            dynamic_cast<UniverseComposite*>(parent)->add(child);
            return;
        default:
            break;
        }
        break;

    //only allow bodies to join to a solarsystem
    case solarsystem:
        switch(child->getType()) {
        case star:
        case blackhole:
        case planet:
            dynamic_cast<UniverseComposite*>(parent)->add(child);
            return;
        default:
            break;
        }
        break;
    default:
        break;
    }

    //anything else is invalid
    qDebug() << "ERROR: cannot add ("
             << child->getName().c_str()
             << ") to ("
             << parent->getName().c_str()
             << "). They are the wrong types. Skipping object.";
    return;

}


UniverseComponent* Config::parseUniverseBlocks()
{
    UniverseComponentFactory factory;
    UniverseComponent* universe = new UniverseComposite(
                UniverseComponentType::universe, "The Universe");

    m_universeComponents.clear();

    //create the components
    for(auto block : m_universeBlocks) {
        try {
            UniverseComponent* component = factory.createUniverseComponent(block);
            if(m_universeComponents.count(component->getName()) > 0)
                qDebug() << "ERROR: name already in use (skipping):" << component->getName().c_str();
            else
                m_universeComponents.insert(make_pair(component->getName(), component));
        }
        catch(invalid_argument e) {
            qDebug() << "ERROR: skipped an object:" << e.what();
        }
        catch(out_of_range e) {
            qDebug() << "ERROR: skipped an object:" << e.what();
        }
        catch(exception e) {
            qDebug() << "ERROR: skipped an object:" << e.what();
        }
    }

    //link the components to their parents (or to the Universe)
    for (auto pair : m_universeComponents) {
        UniverseComponent* child = pair.second;
        UniverseComponent* parent = universe;
        if (child->getParentName() != "") {
            parent = m_universeComponents.at(child->getParentName());
        }
        joinComponents(parent, child);
    }

    return universe;
}

void Config::addToZodiac(const string& labelA, const string& labelB, Zodiac& zodiac)
{
    //find the first object, and make sure it's a suitable type
    if(m_universeComponents.count(labelA) < 1) {
        qDebug() << "ERROR: couldn't find" << labelA.c_str()
                 << "for zodiac, skipping line";
        return;
    }
    UniverseComponent* starA = m_universeComponents.at(labelA);

    switch(starA->getType()) {
    case star:
    case blackhole:
    case planet:
        break;
    default:
        qDebug() << "ERROR:" << labelA.c_str()
                 << "is not a body (for zodiac), skipping line";
        return ;
    }

    //find the second object, and make sure it's a suitable type
    if(m_universeComponents.count(labelB) < 1) {
        qDebug() << "ERROR: couldn't find" << labelB.c_str()
                 << "for zodiac, skipping line";
        return;
    }

    UniverseComponent* starB = m_universeComponents.at(labelB);
    switch(starB->getType()) {
    case star:
    case blackhole:
    case planet:
        break;
    default:
        qDebug() << "ERROR:" << labelB.c_str()
                 << "is not a body (for zodiac), skipping line";
        return ;
    }

    //add a line between the two objects, to the zodiac
    zodiac.add(dynamic_cast<UniverseBody*>(starA),
               dynamic_cast<UniverseBody*>(starB));
}


list<Zodiac>* Config::parseZodiacBlocks()
{
    list<Zodiac>* zodiacs = new list<Zodiac>;
    //for every block of zodiac defined in the file
    for(auto block : m_zodiacBlocks) {
        Zodiac zodiac;
        //for every pair of objects in the block
        for(auto pair : block) {
            //add the line
            addToZodiac(pair.first, pair.second, zodiac);
        }
        //add the finished zodiac
        zodiacs->emplace_back(zodiac);
    }
    return zodiacs;
}
