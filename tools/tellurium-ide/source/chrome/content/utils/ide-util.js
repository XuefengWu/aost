var BrowserDetect = {
	init: function () {
		this.browser = this.searchString(this.dataBrowser) || "An unknown browser";
		this.version = this.searchVersion(navigator.userAgent)
			|| this.searchVersion(navigator.appVersion)
			|| "an unknown version";
		this.OS = this.searchString(this.dataOS) || "an unknown OS";
	},

	searchString: function (data) {
		for (var i=0;i<data.length;i++)	{
			var dataString = data[i].string;
			var dataProp = data[i].prop;
			this.versionSearchString = data[i].versionSearch || data[i].identity;
			if (dataString) {
				if (dataString.indexOf(data[i].subString) != -1)
					return data[i].identity;
			}
			else if (dataProp)
				return data[i].identity;
		}
	},

	searchVersion: function (dataString) {
		var index = dataString.indexOf(this.versionSearchString);
		if (index == -1) return;
		return parseFloat(dataString.substring(index+this.versionSearchString.length+1));
	},

	dataBrowser: [
		{
			string: navigator.userAgent,
			subString: "Chrome",
			identity: "Chrome"
		},
		{ 	string: navigator.userAgent,
			subString: "OmniWeb",
			versionSearch: "OmniWeb/",
			identity: "OmniWeb"
		},
		{
			string: navigator.vendor,
			subString: "Apple",
			identity: "Safari",
			versionSearch: "Version"
		},
		{
			prop: window.opera,
			identity: "Opera"
		},
		{
			string: navigator.vendor,
			subString: "iCab",
			identity: "iCab"
		},
		{
			string: navigator.vendor,
			subString: "KDE",
			identity: "Konqueror"
		},
		{
			string: navigator.userAgent,
			subString: "Firefox",
			identity: "Firefox"
		},
		{
			string: navigator.vendor,
			subString: "Camino",
			identity: "Camino"
		},
		{		// for newer Netscapes (6+)
			string: navigator.userAgent,
			subString: "Netscape",
			identity: "Netscape"
		},
		{
			string: navigator.userAgent,
			subString: "MSIE",
			identity: "Explorer",
			versionSearch: "MSIE"
		},
		{
			string: navigator.userAgent,
			subString: "Gecko",
			identity: "Mozilla",
			versionSearch: "rv"
		},
		{ 		// for older Netscapes (4-)
			string: navigator.userAgent,
			subString: "Mozilla",
			identity: "Netscape",
			versionSearch: "Mozilla"
		}
	],

	dataOS : [
		{
			string: navigator.platform,
			subString: "Win",
			identity: "Windows"
		},
		{
			string: navigator.platform,
			subString: "Mac",
			identity: "Mac"
		},
		{
			   string: navigator.userAgent,
			   subString: "iPhone",
			   identity: "iPhone/iPod"
	    },
		{
			string: navigator.platform,
			subString: "Linux",
			identity: "Linux"
		}
	]

};

//BrowserDetect.init();


/**
 * Returns the node type
 * @param node
 */
function getNodeType(node){
    return node.nodeType;
}

/**
 * Returns the node name
 * @param node
 */
function getNodeName(node){
    return (node &&  node.nodeName) ? node.nodeName : "";
}

/**
 * Returns the node value or innerHTML
 * @param node
 */

function getNodeValue(node){
    return node.nodeValue != null ? node.nodeValue : node.innerHTML;
}


function getAttributeNameOrId(node){
    var attributes = node.attributes;
    var val = "";
     for(var i=0; i < attributes.length; ++i){
        if(attributes[i].name == 'name' || attributes[i].name == 'id'){
            val = attributes[i].value;
            break;
        }
    }
    return val;
}

function findAttributeInList(attributes, attr){
    for(var i=0; i < attributes.length; ++i){
        if(attributes[i].name == attr ){
            val = attributes[i].value;
            break;
        }
    }
}

function createListCell(value){
    var cell = document.createElement("listcell");
    cell.setAttribute("label",value );

    return cell;
}

var XulUtils = {
    clearChildren: function(e) {
        var i;
        for (i = e.childNodes.length - 1; i >= 0; i--) {
            e.removeChild(e.childNodes[i]);
        }
    },

    appendMenuItem: function(e, attributes) {
        var menuitem = document.createElement("menuitem");
        for (key in attributes) {
            if (attributes[key] != null) {
                menuitem.setAttribute(key, attributes[key]);
            }
        }
        e.appendChild(menuitem);
    },

    toXPCOMArray: function(data) {
        var array = Components.classes["@mozilla.org/supports-array;1"].createInstance(Components.interfaces.nsISupportsArray);
        for (var i = 0; i < data.length; i++) {
            array.AppendElement(this.toXPCOMString(data[i]));
        }
        return array;
    },

    toXPCOMString: function(data) {
        var string = Components.classes["@mozilla.org/supports-string;1"].createInstance(Components.interfaces.nsISupportsString);
        string.data = data;
        return string;
    },

    atomService: Components.classes["@mozilla.org/atom-service;1"].
            getService(Components.interfaces.nsIAtomService)
};

/*
XulUtils.TreeViewHelper = classCreate();
objectExtend(XulUtils.TreeViewHelper.prototype, {
    initialize: function(tree) {
        tree.view = this;
        this.tree = tree;
    },

    scrollToRow: function(index) {
        this.treebox.ensureRowIsVisible(index);
    },

    rowUpdated: function(index) {
        this.treebox.invalidateRow(index);
    },

    //
    // nsITreeView interfaces
    //
    setTree: function(treebox) {
        this.log.debug("setTree: treebox=" + treebox);
        this.treebox = treebox;
    },
    isContainer: function(row) {
        return false;
    },
    isSeparator: function(row) {
        return false;
    },
    isSorted: function(row) {
        return false;
    },
    getLevel: function(row) {
        return 0;
    },
    getImageSrc: function(row, col) {
        return null;
    },
    getColumnProperties: function(colid, col, props) {
    },
    cycleHeader: function(colID, elt) {
    }
});
*/

function getAvailableContentDocumentUrls(aChrome){
    var ww = Components.classes["@mozilla.org/appshell/window-mediator;1"].getService(Components.interfaces["nsIWindowMediator"]);
    var windows = ww.getXULWindowEnumerator(null);
    var docs = [];
    while (windows.hasMoreElements()) {
      try {
        // Get the window's main docshell
        var windowDocShell = windows.getNext().QueryInterface(Components.interfaces["nsIXULWindow"]).docShell;
        appendContainedDocumentUrls(docs, windowDocShell,
                                      aChrome ?
                                        Components.interfaces.nsIDocShellTreeItem.typeChrome :
                                        Components.interfaces.nsIDocShellTreeItem.typeContent);
      }
      catch (ex) {
        // We've failed with this window somehow, but we're catching the error
        // so the others will still work
        logger.error("getAvailableContentDocuments failed:\n" + describeErrorStack(ex));
      }
    }

    return docs;
}

function appendContainedDocumentUrls(array, docShell, type)
{
    // Load all the window's content docShells
    var containedDocShells = docShell.getDocShellEnumerator(type, Components.interfaces.nsIDocShell.ENUMERATE_FORWARDS);
    while (containedDocShells.hasMoreElements()) {
        try {
            // Get the corresponding document for this docshell
            var childDoc =  containedDocShells.getNext().QueryInterface(Components.interfaces["nsIDocShell"]).contentViewer.DOMDocument;

            // Ignore the DOM Inspector's browser docshell if it's not being used
            if (docShell.contentViewer.DOMDocument.location.href !=
                    document.location.href ||
                    childDoc.location.href != "about:blank") {
                array.push(childDoc.location.href);
            }
        }
        catch (ex) {
            logger.error("getAvailableContentDocuments failed:\n" + describeErrorStack(ex));
        }
    }
}

function getAvailableContentDocuments(aChrome){
    var ww = Components.classes["@mozilla.org/appshell/window-mediator;1"].getService(Components.interfaces["nsIWindowMediator"]);
    var windows = ww.getXULWindowEnumerator(null);
    var docs = [];
    while (windows.hasMoreElements()) {
      try {
        // Get the window's main docshell
        var windowDocShell = windows.getNext().QueryInterface(Components.interfaces["nsIXULWindow"]).docShell;
        appendContainedDocuments(docs, windowDocShell,
                                      aChrome ?
                                        Components.interfaces.nsIDocShellTreeItem.typeChrome :
                                        Components.interfaces.nsIDocShellTreeItem.typeContent);
      }catch (ex) {
        // We've failed with this window somehow, but we're catching the error
        // so the others will still work
        logger.error("getAvailableContentDocuments failed:\n" + describeErrorStack(ex));
      }
    }

    return docs;
}

function appendContainedDocuments(array, docShell, type)
{
    // Load all the window's content docShells
    var containedDocShells = docShell.getDocShellEnumerator(type, Components.interfaces.nsIDocShell.ENUMERATE_FORWARDS);
    while (containedDocShells.hasMoreElements()) {
        try {
            // Get the corresponding document for this docshell
            var childDoc =  containedDocShells.getNext().QueryInterface(Components.interfaces["nsIDocShell"]).contentViewer.DOMDocument;

            // Ignore the DOM Inspector's browser docshell if it's not being used
            if (docShell.contentViewer.DOMDocument.location.href !=
                    document.location.href ||
                    childDoc.location.href != "about:blank") {
                array.push(childDoc);
            }
        }catch (ex) {
            logger.error("getAvailableContentDocuments failed:\n" + describeErrorStack(ex));
        }
    }
}

/**
 * Opens the given URL in a new tab if a browser window is already open, or
 * a new window otherwise.
 *
 * @param url  the URL to open.
 */
function openTabOrWindow(url)
{
    try {
        var gBrowser = window.opener.getBrowser();
        gBrowser.selectedTab = gBrowser.addTab(url);
    }
    catch (e) {
        window.open(url);
    }
}
