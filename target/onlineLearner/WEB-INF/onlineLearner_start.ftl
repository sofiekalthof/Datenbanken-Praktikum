<html>
<head><title>OnlineLearner</title>
<style type="text/css">
* {
   margin:0;
   padding:0;
}

body{
   text-align:center;
   background: #efe4bf none repeat scroll 0 0;
}

#wrapper{
   width:960px;
   margin:0 auto;
   text-align:middle;
   background-color: #fff;
   border-radius: 0 0 10px 10px;
   padding: 20px;
   box-shadow: 1px -2px 14px rgba(0, 0, 0, 0.4);
}

#header{
 color: #fff;
 background-color: #2c5b9c;
 height: 3.5em;
 padding: 1em 0em 1em 1em;
 
}

#site{
    background-color: #fff;
    padding: 20px 0px 0px 0px;
}
.centerBlock{
	margin:0 auto;
}
</style>

<body>
	<div id="wrapper">
		<div id="header">
		<h1> OnlineLearner Website </h1>
		</div>
	   
		<div id="site">
		<p>
			<h1> Meine Kurse </h1>
			
			<#list myCourses as course>
			<p>
				<a href="view_course?course=${course.name}"> <h2>${course.name}</h2> </a>
				Ersteller:${course.ersteller}<br>
				Freie Plätze: ${course.freiePlaetze}<br> <br>
			</p>
			</#list>
			
			<h1> Verfügbare Kurse </h1>
			
			<#list courses as course>
			<p>
				<a href="view_course?course=${course.name}" > <h2>${course.name}</h2> </a>
				Ersteller:${course.ersteller}<br>
				Freie Plätze: ${course.freiePlaetze}<br> <br>
			</p>
			</#list>
			<a href="createCourse"><button> Kurs erstellen </button> </a>
		</p>
		</div>
	</div>
</body>
</html>
