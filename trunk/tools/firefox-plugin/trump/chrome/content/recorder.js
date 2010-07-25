
function Recorder(window) {
    this.window = window;

    this.frames = null;
    this.frameName = null;

    this.contentWindow = null;

    this.parentWindow = this.window.opener;
    this.builder = new Builder();
    this.decorator = new Decorator();
    this.listener = null;
    this.selectedElements = new Array();
    this.tagObjectArray = new Array();

    this.treeView = TreeView;
    this.tree = document.getElementById('recordTree');
    this.tree.view=this.treeView;

    this.workspace = null;

    this.sequence = new Identifier();
    
//    this.observers = [];
//	this.attach();
//    this.registerUnloadListener();
}

Recorder.WINDOW_RECORDER_PROPERTY = "_Trump_IDE_Recorder";

Recorder.prototype.attachActionListeners = function(window){
    logger.debug("Attaching listeners for action...");
//    teJQuery(window.document.body).bind("click", {recorder: this}, this.uiSelectListener);
    teJQuery(window).bind("beforeunload", {recorder: this}, this.onUnloadDocumentListener);
    teJQuery(window.document, ":input, a, select, button").live("change", {recorder: this}, this.typeListener);
    teJQuery(window.document, ":input, a, select, button").live("click", {recorder: this}, this.clickListener);
    teJQuery(window.document, "select").live("focus", {recorder: this}, this.selectFocusListener);
    teJQuery(window.document, "select").live("mousedown", {recorder: this}, this.selectMousedownListener);
    teJQuery(window.document, "select").live("change", {recorder: this}, this.selectListener);
};

Recorder.prototype.detachActionListeners = function(window){
    logger.debug("Detaching listeners for action...");
//    teJQuery(window.document.body).unbind("click", this.uiSelectListener);
    teJQuery(window).unbind("beforeunload", this.onUnloadDocumentListener);
    teJQuery(window.document, ":input, a, select, button").die("change", this.typeListener);
    teJQuery(window.document, ":input, a, select, button").die("click", this.clickListener);
    teJQuery(window.document, "select").die("focus", this.selectFocusListener);
    teJQuery(window.document, "select").die("mousedown", this.selectMousedownListener);
    teJQuery(window.document, "select").die("change", this.selectListener);
};

Recorder.prototype.attachSelectListeners = function(window){
    logger.debug("Attaching listeners for selection");
    teJQuery(window.document.body).bind("click", {recorder: this}, this.uiSelectListener);
};

Recorder.prototype.detachSelectListeners = function(window){
    logger.debug("Detaching listeners for selection");
    teJQuery(window.document.body).unbind("click", this.uiSelectListener);
};

Recorder.prototype.registerListeners = function(){
    var win = Components.classes["@mozilla.org/appshell/window-mediator;1"]
        .getService(Components.interfaces.nsIWindowMediator)
          .getMostRecentWindow("navigator:browser");

    var browser = win.getBrowser();

    if(browser && browser.contentWindow && browser.contentWindow.document){
        this.contentWindow = browser.contentWindow;
        if(this.isAction())
            this.attachActionListeners(this.contentWindow);
        else
            this.attachSelectListeners(this.contentWindow);
    }

    if(browser && browser.contentWindow && browser.contentWindow.frames){
        this.frames = browser.contentWindow.frames;
        if (this.frames && this.frames.length) {
            for (var j = 0; j < this.frames.length; j++) {
                var frame = this.frames[j] ;
                if(this.isAction())
                    this.attachActionListeners(frame);
                else
                    this.attachSelectListeners(frame);
            }
        }
    }
};

Recorder.prototype.isAction = function(){
    var recordToolbarButton = document.getElementById("record-button");
    var state = recordToolbarButton.getAttribute("class");

    return state == RecordState.RECORD;
};

Recorder.prototype.unregisterListeners = function() {
    this.removeBackgroundForSelectedNodes();

    this.removeOutlineForSelectedNodes();

    if (this.contentWindow) {
        this.detachActionListeners(this.contentWindow);
        this.detachSelectListeners(this.contentWindow);
    }

    if (this.frames && this.frames.length) {
        for (var j = 0; j < this.frames.length; j++) {
            this.detachActionListeners(this.frames[j]);
            this.detachSelectListeners(this.frames[j]);
        }
    }
};

Recorder.prototype.stopRecording = function(){
    
};

Recorder.prototype.showSelectedNode = function(){

    this.removeOutlineForSelectedNodes();
    
    var node = this.selectedElements[this.tree.currentIndex];
    this.decorator.addOutline(node);

};

Recorder.prototype.removeBackgroundForSelectedNodes = function(){
    for(var i=0; i< this.selectedElements.length ; ++i){
        this.decorator.removeBackground(this.selectedElements[i]);
    }
};

Recorder.prototype.removeOutlineForSelectedNodes = function(){
    for(var i=0; i< this.selectedElements.length ; ++i){
        this.decorator.removeOutline(this.selectedElements[i]);
    }
};

Recorder.prototype.clearAll = function(){
    this.removeOutlineForSelectedNodes();
    this.removeBackgroundForSelectedNodes();
    
    this.selectedElements = new Array();
    this.tagObjectArray = new Array();
    this.treeView.clearAll();    
};

Recorder.prototype.recordDomNode = function (element){
    //check if the element is already selected
    var index = this.selectedElements.indexOf(element);
    if (index == -1) {
        this.decorator.addBackground(element);
        this.selectedElements.push(element);

        var uid = "trumpSelected" + this.sequence.next();
        var tagObject = this.builder.createTagObject(element, uid, this.frameName);
        teJQuery(element).data("sid", uid);
        this.tagObjectArray.push(tagObject);

        this.treeView.setTagObjects(this.tagObjectArray);
        this.treeView.rowInserted();
        this.workspace.addNode(element, this.frameName, uid);
    } else {
        //we are assuming to remove the element
        this.decorator.removeBackground(element);
        this.selectedElements.splice(index, 1);
        this.tagObjectArray.splice(index, 1);
        this.treeView.deleteRow(index);
        teJQuery(element).removeData("sid");
    }
    var baseUrl = document.getElementById("windowURL").value;
    var actualUrl = element.ownerDocument.location.href;
    if (baseUrl.trim().length == 0 || baseUrl != actualUrl) {
        document.getElementById("windowURL").value = actualUrl;
    }        
};

Recorder.prototype.recordCommand = function(name, element, value){
    this.recordDomNode(element);
    logger.debug("Recording command (name: " + name + ", element: " + element.tagName + ", value: " + value);
};

Recorder.prototype.findClickableElement = function(e) {
	if (!e.tagName) return null;
	var tagName = e.tagName.toLowerCase();
	var type = e.type;
	if (e.hasAttribute("onclick") || e.hasAttribute("href") || tagName == "button" ||
		(tagName == "input" &&
		 (type == "submit" || type == "button" || type == "image" || type == "radio" || type == "checkbox" || type == "reset"))) {
		return e;
	} else {
		if (e.parentNode != null) {
			return this.findClickableElement(e.parentNode);
		} else {
			return null;
		}
	}
};

Recorder.prototype.callIfMeaningfulEvent = function(handler) {
    logger.debug("callIfMeaningfulEvent");
    this.delayedRecorder = handler;
    var self = this;
    this.domModifiedTimeout = setTimeout(function() {
        logger.debug("clear event");
        self.delayedRecorder = null;
        self.domModifiedTimeout = null;
    }, 50);
};

Recorder.eventHandlers = {};
