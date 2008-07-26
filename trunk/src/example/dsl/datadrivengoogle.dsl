//define google start page
ui.Container(uid: "google_start_page", clocator: [tag: "td"], group: "true"){
    InputBox(uid: "searchbox", clocator: [title: "Google Search"])
    SubmitButton(uid: "googlesearch", clocator: [name: "btnG", value: "Google Search"])
    SubmitButton(uid: "Imfeelinglucky", clocator: [value: "I'm Feeling Lucky"])
}

//AOST has already provided default type handlers for regular Java type
//The users can also define custom data types and their type handlers so that they
//can read and process special types of data
typeHandler "phoneNumber", "example.aost.PhoneNumberTypeHandler"

//define file data format for each line, always start with "fs."
fs.FieldSet(name: "fs4googlesearch", description: "example field set for google search"){
    //define fields for each line
    Field(name: "regularSearch", type: "boolean", description: "whether we should use regular search or use I'm feeling lucky")
    Field(name: "phoneNumber", type: "phoneNumber", description: "Phone number")
    Field(name: "input", description: "input variable")
}

//load file
loadData "src/example/dsl/googlesearchinput.txt"

stepOneLine{

    //bind variables
    String input = bind("input")

    openUrl "http://www.google.com"
    type "google_start_page.searchbox", input
    pause 500
}

stepOverOneLine

//read each line and execute the script until the end of the file
stepToEnd {
    //bind variables
    //Since there is only one FieldSet, the fieldSet Id can be omitted
    boolean regularSearch = bind("regularSearch")
    //But if you have more than one FieldSets, you must specify the FieldSet Id as follows
    def phoneNumber = bind("fs4googlesearch.phoneNumber")
    String input = bind("input")

    openUrl "http://www.google.com"
    type "google_start_page.searchbox", input
    pause 500
    
    if(regularSearch)
       click "google_start_page.googlesearch"
    else
       click "google_start_page.Imfeelinglucky"

    pause 1000

    openUrl "http://www.google.com"
    type "google_start_page.searchbox", phoneNumber
    click "google_start_page.Imfeelinglucky"

    pause 1000
}

//close file
closeData