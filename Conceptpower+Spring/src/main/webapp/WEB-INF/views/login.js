

function validate(){
var username = document.getElementById("username").value;
var password = document.getElementById("password").value;
var navbar = document.getElementById("rightNavSection");
var btn = document.getElementById("signInBtn");
navbar.style.display=none;
if ( username == "admin" && password == "admin"){
alert ("Login successfully");
btn.style.display=none;
navbar.style.display=contents;
return false;
}
else{
attempt --;// Decrementing by one.
alert("You have left "+attempt+" attempt;");
// Disabling fields after 3 attempts.
if( attempt == 0){
document.getElementById("username").disabled = true;
document.getElementById("password").disabled = true;
document.getElementById("submit").disabled = true;
return false;
}
}
}


