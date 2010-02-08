
function Recorder(window) {
//    alert("Recorder")
    this.window = window;
    this.parentWindow = this.window.opener;
    this.builder = new Builder();
    this.decorator = new Decorator();
    this.listener = null;
    this.selectedElements = new Array();
    this.tagObjectArray = new Array();

    this.treeView = TreeView;
    this.tree = document.getElementById('recordTree');
    this.tree.view=this.treeView;

}

Recorder.WINDOW_RECORDER_PROPERTY = "_TrUMP_IDE_Recorder";

Recorder.prototype.registerListener = function(){
    this.registerClickListener();
}

Recorder.prototype.unregisterListener = function(){
    this.unregisterClickListener();
}

Recorder.prototype.registerClickListener = function(){
    var self = this;
    this.listener =
        function(event){
            event.preventDefault();
            var element = event.target;
            //check if the element is already selected
            var index = self.selectedElements.indexOf(element);
            if(index == -1){
                self.decorator.addBackground(element);
                self.selectedElements.push(element);

                var tagObject = self.builder.createTagObject(element);

                self.tagObjectArray.push(tagObject);

                self.treeView.setTagObjects(self.tagObjectArray);
                self.treeView.rowInserted();

            }else{
                //we are assuming to remove the element
                self.decorator.removeBackground(element);
                self.selectedElements.splice(index, 1);
                self.tagObjectArray.splice(index, 1);
                self.treeView.deleteRow(index);
            }

        };
//    this.parentWindow.document.addEventListener("click", this.listener, false);
    this.parentWindow.content.document.body.addEventListener("click", this.listener, false);
}

Recorder.prototype.unregisterClickListener = function(){
    this.removeBackgroundForSelectedNodes();

    this.removeOutlineForSelectedNodes();

//    this.parentWindow.document.removeEventListener("click", this.listener, false);
    this.parentWindow.content.document.body.removeEventListener("click", this.listener, false);
    this.listener = null;
}

Recorder.prototype.stopRecording = function(){
    
}

Recorder.prototype.showSelectedNode = function(){

    this.removeOutlineForSelectedNodes();
    
    var node = this.selectedElements[this.tree.currentIndex];
    this.decorator.addOutline(node);

}

Recorder.prototype.removeBackgroundForSelectedNodes = function(){
    for(var i=0; i< this.selectedElements.length ; ++i){
        this.decorator.removeBackground(this.selectedElements[i]);
    }
}

Recorder.prototype.removeOutlineForSelectedNodes = function(){
    for(var i=0; i< this.selectedElements.length ; ++i){
        this.decorator.removeOutline(this.selectedElements[i]);
    }
}

Recorder.prototype.clearAll = function(){
    this.removeOutlineForSelectedNodes();
    this.removeBackgroundForSelectedNodes();
    
    this.selectedElements = new Array();
    this.tagObjectArray = new Array();
    this.treeView.clearAll();    
}
