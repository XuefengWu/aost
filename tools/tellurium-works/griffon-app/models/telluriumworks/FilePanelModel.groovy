package telluriumworks

import groovy.beans.Bindable
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport
import java.beans.PropertyChangeEvent
import griffon.beans.Listener

class FilePanelModel{

   String mvcId

   @Bindable Document document
}
