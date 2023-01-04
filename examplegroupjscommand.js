function getName(){
    return "testjs";
}

function process(arguments,bot,target,event){
    var message = "";
    for (var i = 0; i < arguments.length; i++) {
        message+=arguments[i];
    }
    target.sendMessage(message);
}
