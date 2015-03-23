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

function setError(elem, message) {
    $( "#" + elem.id ).addClass("error");
    $( "#" + elem.id.split("_")[0] + "_error").removeClass("hidden").addClass("error_text").text(message);
}

function removeError(elem) {
    $( "#" + elem.id ).removeClass("error");
    $( "#" + elem.id.split("_")[0] + "_error").addClass("hidden").removeClass("error_text").text("");
}

function validateDate(elem) {
    console.log("validateDate: " + elem.value);
    var ret = true
    if (isValidDate(elem.value)) {
        removeError(elem);
    } else {
        setError(elem, "Invalid date");
        ret = false;
    }
    return ret;
};

function validateString(elem) {
    var ret = true
    console.log("validateString: " + elem.value);
    if (elem.value == null || elem.value == "") {
        setError(elem, "invalid entry");
        ret = false;
    } else {
        removeError(elem);
    }
    return ret;
};

function validateNumber(elem) {
    var ret = true
    console.log("validateNumber: " + elem.value);
    if (elem.value == null || elem.value == "" || elem.value <= 0) {
        setError(elem, "Invalid Number");
        ret = false;
    } else {
        removeError(elem);
    }
    return ret;
};

function log(message) {
    console.log(message);
}

function validateForm(elem_ids) {
    log("startFormValidation");
    var idsLength = elem_ids.length;
    var elementsValid = true;
    for (var i = 0; i < idsLength; i++) {
        var id = "#" + elem_ids[i];
        var elem = $( id )[0];
        log("validating " + id )
        switch (elemType(id)) {
        case 'str':
            log("validate_form: processing string");
            if (!validateString(elem)) {
                elementsValid = false;
            }
            break;
        case 'num':
            log("validate_form: processing number");
            if (!validateNumber(elem)) {
                elementsValid = false;
            }
            break;
        case 'date':
            log("validate_form: processing date");
            if (!validateString(elem)) {
                elementsValid = false;
            }
            break;
        default:
            alert("element" + id + " has unknown element type");
            break;
        }
    }
    return elementsValid;
}

function elemType(id) {
    var type = "";
    var parts = id.split("_");
    if (parts.length >= 0) {
        type = parts[parts.length - 1];
    }
    return type;
}