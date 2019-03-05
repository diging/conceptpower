<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<script>
$(function() {
	   /*  Submit form using Ajax */
	   $('button[type=submit]').click(function(e) {
	   
	      //Prevent default submission of form
	      e.preventDefault();
	     
	      $.post({
	         url : '${pageContext.servletContext.contextPath}/auth/addComment',
	         data : $('form[name=reviewForm]').serialize(),
	         success : function(res) {
	         
	  
	               //Set response
	               $('#resultContainer pre code').text(JSON.stringify(res.employee));
	               $('#resultContainer').show();
	            
	           
	         }
	      })
	   });
	});
	
$('#reviewForm').submit(function(e) {
	
	console.log("inside Ajax");
    // reference to form object
var form = this;
    // for stopping the default action of element
    e.preventDefault();
    // mapthat will hold form data
    var formData = {}
//iterate over form elements   
    $.each(this, function(i, v){
    var input = $(v);
    // populate form data as key-value pairs
        // with the name of input as key and its value as value
    formData[input.attr("name")] = input.val();
    });
    $.ajax({
        type: form.attr('method'), // method attribute of form
        url: form.attr('action'),  // action attribute of form
        dataType : 'json',
    // convert form data to json format
        data : JSON.stringify(formData),
    });
});
	
$('#reviewForm').submit(function(e) {
	
	console.log("inside Ajax");
    // reference to form object
var form = this;
    // for stopping the default action of element
    e.preventDefault();
    // mapthat will hold form data
    var formData = {}
//iterate over form elements   
    $.each(this, function(i, v){
    var input = $(v);
    // populate form data as key-value pairs
        // with the name of input as key and its value as value
    formData[input.attr("name")] = input.val();
    });
    $.ajax({
        type: form.attr('method'), // method attribute of form
        url: form.attr('action'),  // action attribute of form
        dataType : 'json',
    // convert form data to json format
        data : JSON.stringify(formData),
        success : function(res) {
            
            if(res.validated){
               //Set response
               $('#resultContainer pre code').text(JSON.stringify(res.employee));
               $('#resultContainer').show();
            
            }else{
              //Set error messages
              $.each(res.errorMessages,function(key,value){
  	            $('input[name='+key+']').after('<span class="error">'+value+'</span>');
              });
            }
        }
    });
});

	</script>

<p> ${comment} <p>
</body>
</html>