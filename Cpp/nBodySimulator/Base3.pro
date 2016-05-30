#-------------------------------------------------
#
# Project created by QtCreator 2016-05-10T21:34:55
#
#-------------------------------------------------

QT       += core gui

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

CONFIG += c++11

TARGET = Base3
TEMPLATE = app


SOURCES += main.cpp\
        dialog.cpp \
    universebody.cpp \
    universecomposite.cpp \
    config.cpp \
    zodiac.cpp \
    universecomponentfactory.cpp \
    adjustvisitor.cpp \
    censusvisitor.cpp \
    visitor.cpp

HEADERS  += dialog.h \
    universecomponent.h \
    universecomposite.h \
    universebody.h \
    config.h \
    zodiac.h \
    universecomponentfactory.h \
    visitor.h \
    adjustvisitor.h \
    censusvisitor.h \
    form.h \
    memento.h \
    state.h

FORMS    += dialog.ui \
    planetinfo.ui

OTHER_FILES +=
