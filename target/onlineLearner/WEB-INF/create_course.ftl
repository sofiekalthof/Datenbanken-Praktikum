<html>
<head><title>Create_course</title>
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
   text-align:left;
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
		<h1> Kurs erstellen </h1>
		</div>
		<div id="site">
			<form name="create_course" action="createCourse" method="post">
				Name: <input type ="text" name="name" /> <br/> <br/>
				Einschreibeschlüssel: <input type= "text" name="schluessel" /> <br/> <br/>
				Anz. freie Plätze: <input type="number" name="plaetze" step="1" /> <br/> <br/>
				Beschreibungstext: <textarea name="beschreibung" rows="4" cols="30"> </textarea> <br/> <br/>
				<input type="submit" value="Erstellen" />
			</form>
			<a href="onlineLearner"><button> Zurück zur Startseite </button> </a>
			
			
		</div>
	</div>	
</body>
</html>