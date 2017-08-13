#-------------------------------------------------
#
# Project created by QtCreator 2017-05-30T14:55:16
#
#-------------------------------------------------

QT       += core gui

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = myMDI
TEMPLATE = app


SOURCES += main.cpp\
        mainwindow.cpp \
    mdichild.cpp

HEADERS  += mainwindow.h \
    mdichild.h

FORMS    += mainwindow.ui

RESOURCES += \
    myimage.qrc
