
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

    this.sequence = new Identifier();
}

Recorder.WINDOW_RECORDER_PROPERTY = "_TrUMP_IDE_Recorder";

Recorder.prototype.registerListener = function(){
    this.registerClickListener();
};

Recorder.prototype.unregisterListener = function(){
    this.unregisterClickListener();
};

Recorder.prototype.updateListenerForWindow = function(url){
    this.unregisterListener();
    this.reRegisterClickListener(url);
};

Recorder.prototype.registerClickListener = function() {
    var self = this;
    this.listener =
            function(event) {
                event.preventDefault();
                var element = event.target;
                //check if the element is already selected
                var index = self.selectedElements.indexOf(element);
                if (index == -1) {
                    self.decorator.addBackground(element);
                    self.selectedElements.push(element);

                    var uid = "trumpSelected" + self.sequence.next();
                    var tagObject = self.builder.createTagObject(element, uid, self.frameName);
                    teJQuery(element).data("sid", uid);
                    self.tagObjectArray.push(tagObject);

                    self.treeView.setTagObjects(self.tagObjectArray);
                    self.treeView.rowInserted();

                } else {
                    //we are assuming to remove the element
                    self.decorator.removeBackground(element);
                    self.selectedElements.splice(index, 1);
                    self.tagObjectArray.splice(index, 1);
                    self.treeView.deleteRow(index);
                    teJQuery(element).removeData("sid");
                }

                var baseUrl = document.getElementById("windowURL").value;
                var actualUrl = element.ownerDocument.location.href;
                if (baseUrl.trim().length == 0 || baseUrl != actualUrl) {
                    document.getElementById("windowURL").value = actualUrl;
                }
            };


    this.frameFocusListener =
            function(event) {
                event.preventDefault();
                //            logger.debug("Inside frameFocusListener() ..");
                if (self.frameName == null) {
                    //                logger.debug("frameName : " + event.target.name);
                    self.frameName = event.target.name;
                }
            };

    this.frameBlurListener = function(event) {
        event.preventDefault();
        self.frameName = null;
    };

    this.getWindowAndRegisterClickListener();

};

Recorder.prototype.getWindowAndRegisterClickListener = function(){
    var win = Components.classes["@mozilla.org/appshell/window-mediator;1"]
        .getService(Components.interfaces.nsIWindowMediator)
          .getMostRecentWindow("navigator:browser");

    var browser = win.getBrowser();

    if(browser && browser.contentWindow && browser.contentWindow.document){
        this.contentWindow = browser.contentWindow;
        this.contentWindow.document.body.addEventListener("click", this.listener, false);
    }

    if(browser && browser.contentWindow && browser.contentWindow.frames){
        this.frames = browser.contentWindow.frames;
        if (this.frames && this.frames.length) {
            for (var j = 0; j < this.frames.length; j++) {
                var frame = this.frames[j] ;
                frame.document.body.addEventListener("click", this.listener, false);
                frame.addEventListener("focus", this.frameFocusListener, false);
                frame.addEventListener("blur", this.frameBlurListener, false);
            }
        }
    }

};

Recorder.prototype.reRegisterClickListener = function(url) {
    var self = this;
    this.listener =
            function(event) {
                event.preventDefault();
                var element = event.target;
                //check if the element is already selected
                var index = self.selectedElements.indexOf(element);
                if (index == -1) {
                    self.decorator.addBackground(element);
                    self.selectedElements.push(element);

                    var uid = "trumpSelected" + self.sequence.next();
                    var tagObject = self.builder.createTagObject(element, uid, self.frameName);
                    teJQuery(element).data("sid", uid);
                    self.tagObjectArray.push(tagObject);

                    self.treeView.setTagObjects(self.tagObjectArray);
                    self.treeView.rowInserted();

                } else {
                    //we are assuming to remove the element
                    self.decorator.removeBackground(element);
                    self.selectedElements.splice(index, 1);
                    self.tagObjectArray.splice(index, 1);
                    self.treeView.deleteRow(index);
                    teJQuery(element).removeData("sid");
                }

                var baseUrl = document.getElementById("windowURL").value;
                var actualUrl = element.ownerDocument.location.href;
                if (baseUrl.trim().length == 0 || baseUrl != actualUrl) {
                    document.getElementById("windowURL").value = actualUrl;
                }
            };


    this.frameFocusListener =
            function(event) {
                event.preventDefault();
    //            logger.debug("Inside frameFocusListener() ..");
                if (self.frameName == null) {
    //                logger.debug("frameName : " + event.target.name);
                    self.frameName = event.target.name;
                }
            };

    this.frameBlurListener = function(event) {
        event.preventDefault();
        self.frameName = null;
    };

    this.updateWindowAndRegisterClickListener(url);

};

Recorder.prototype.updateWindowAndRegisterClickListener = function(url){
    var cWindows = getAvailableContentDocuments(Components.interfaces.nsIDocShellTreeItem.typeChrome);
//    var cWindows = getAvailableContentDocuments(Components.interfaces.nsIDocShellTreeItem.typeContent);
    var cWin = null;
    if(cWindows != null && cWindows.length > 0){
        for(var i=0; i<cWindows.length; i++){
            if(url == cWindows[i].location.href){
                cWin = cWindows[i];
                break;
            }
        }
    }

    if (cWin != null) {
        this.contentWindow = cWin;
        this.contentWindow.document.body.addEventListener("click", this.listener, false);

        if(cWin.frames){
            this.frames = cWin.frames;
            if (this.frames && this.frames.length) {
                for (var j = 0; j < this.frames.length; j++) {
                    var frame = this.frames[j];
                    frame.document.body.addEventListener("click", this.listener, false);
                    frame.addEventListener("focus", this.frameFocusListener, false);
                    frame.addEventListener("blur", this.frameBlurListener, false);
                }
            }
        }
/*        var browser = win.getBrowser();

        if (browser && browser.contentWindow && browser.contentWindow.document) {
            this.contentWindow = browser.contentWindow;
            this.contentWindow.document.body.addEventListener("click", this.listener, false);
        }

        if (browser && browser.contentWindow && browser.contentWindow.frames) {
            this.frames = browser.contentWindow.frames;
            if (this.frames && this.frames.length) {
                for (var j = 0; j < this.frames.length; j++) {
                    var frame = this.frames[j];
                    frame.document.body.addEventListener("click", this.listener, false);
                    frame.addEventListener("focus", this.frameFocusListener, false);
                    frame.addEventListener("blur", this.frameBlurListener, false);
                }
            }
        }*/
    } else {
        logger.error("Invalid Window URL: " + url);
    }
};

Recorder.prototype.unregisterClickListener = function(){
    this.removeBackgroundForSelectedNodes();

    this.removeOutlineForSelectedNodes();

    if(this.contentWindow){
        this.contentWindow.document.body.removeEventListener("click", this.listener, false);
    }

    if (this.frames && this.frames.length) {
        for (var j = 0; j < this.frames.length; j++) {
            this.frames[j].document.body.removeEventListener("click", this.listener, false);
        }
    } 
    
    this.listener = null;
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