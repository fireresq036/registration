function isValidDate(dateString)
{
    // First check for the pattern
    if(!/^\d{1,2}\/\d{1,2}\/\d{4}$/.test(dateString))
        return false;

    // Parse the date parts to integers
    var parts = dateString.split("/");
    var day = parseInt(parts[1], 10);
    var month = parseInt(parts[0], 10);
    var year = parseInt(parts[2], 10);

    // Check the ranges of month and year
    if(year < 1000 || year > 3000 || month == 0 || month > 12)
        return false;

    var monthLength = [ 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 ];

    // Adjust for leap years
    if(year % 400 == 0 || (year % 100 != 0 && year % 4 == 0))
        monthLength[1] = 29;

    // Check the range of the day
    return day > 0 && day <= monthLength[month - 1];
};

function showSubmitButton(buttonId) {
    if ($( ".error" ).length > 0) {
        $( "#" + buttonId).prop("disabled",true);
    } else {
        $( "#" + buttonId).prop("disabled",false);
    }
}
function setError(elem, message) {
    $( "#" + elem.id ).addClass("error");
    $( "#" + elem.id + "_error").removeClass("hidden").addClass("error_text").text(message);
}

function removeError(elem) {
    $( "#" + elem.id ).removeClass("error");
    $( "#" + elem.id + "_error").addClass("hidden").removeClass("error_text");
}

function validateDate(elem, buttonId) {
    if (isValidDate(elem.value)) {
        removeError(elem);
    } else {
        setError(elem, "Invalid date");
    }
    showSubmitButton(buttonId);
};

function validateString(elem, buttonId) {
    if (elem.value > 0 || elem.value == "") {
        setError(elem, "invalid entry");
    } else {
        removeError(elem);
    }
    showSubmitButton(buttonId);
};

function validateNumber(elem, buttonId) {
    if (elem.value == null || elem.value == "" || elem.value <= 0) {
        setError(elem, "Invalid Number");
    } else {
        removeError(elem);
    }
    showSubmitButton(buttonId);
};