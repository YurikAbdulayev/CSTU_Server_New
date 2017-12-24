$(document).ready(function() {
    $("#button").click(createUser)
});

function createUser() {
    var firstName = $("#firstName").val();
    var lastName = $("#lastName").val();
    var email = $("#email").val();
    //var firstName = document.getElemtentById("fistname").value;
    $.post("/createUser", {
        fName: firstName,
        lName: lastName,
        e: email
    }, function(data) {
        var json = JSON.parse(data);
        alert("Status - " + json.status);
    })
}