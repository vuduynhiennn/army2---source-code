<?php
if ($login || !$connect)
    exit(0);
include 'res/head.php';
$error = '<div style="color: red;">* Đăng nhập không thành công.</div>'. "\n". '';

if ($_POST) {
    $uname = $_POST['uname'];
    $passw = $_POST['passw'];
    
    if ($uname != NULL && $passw != NULL) {
        $sql = "SELECT * FROM `user` WHERE (`user` = '". $conn->real_escape_string($uname) ."' AND `password` = '". $conn->real_escape_string($passw) ."') LIMIT 1;";
        $result = $conn->query($sql);
        if ($result->num_rows > 0) {
            $user = $result->fetch_assoc();
            $_SESSION['uid'] = $user['user_id'];
            $_SESSION['user_id'] = $user['user_id'];
            header('Location: ?c=fr');
        }
    }
    
    echo $error;
} 

?>
<form action="" method="post">
    ID:<input type="text" name="uname" />
    Password:<input type="password" name="passw" />
    <input type="submit" value="Đăng nhập" />
</form>
<?php
include 'res/end.php';
?>