function checkSendForm() {
    if (!((document.getElementById('textareaText').value).trim())) {
        document.getElementById('lineTextarea').className = "form-group has-error";
        return false;
    }
    document.getElementById('to').disabled = false;
    return true;
}


function changeTemplate() {
    document.getElementById('lineTextarea').className = "form-group";
    if ('-None selected-' == document.getElementById('templateSelect').value) {
        document.getElementById('textareaText').disabled = false;
        document.getElementById('textareaText').value = "";
    } else {
        document.getElementById('textareaText').value = document.getElementById('id' + document.getElementById('templateSelect').value).value;
        document.getElementById('textareaText').disabled = true;
    }
}