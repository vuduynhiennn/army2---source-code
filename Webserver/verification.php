<?php
$get = $_GET;
include './include/config.php';
include './include/connect.php';
echo '<meta charset="UTF-8" />';
if ($get['type'] == 'active') {
  $token    = addslashes($get['token']);
  $email    = addslashes($get['email']);
  if ($token == null || $email == null) {
    echo '<br>Kích hoạt thất bại!';
    exit;
  }
  $sql = "UPDATE `user` SET `active` = 1, `token` = '' WHERE `email` = '".$game->real_escape_string($email)."' AND `token` = '".$game->real_escape_string($token)."';";
  $game->query($sql);
  if ($game->affected_rows > 0) {
    echo '<br>Kích hoạt tài khoản thành công!';
  } else {
    echo '<br>Kích hoạt tài khoản thất bại!';
  }
} else if ($get['type'] == 'changeemail') {
  $token    = addslashes($get['token']);
  $email    = addslashes($get['email']);
  $username = addslashes($get['username']);
  if ($token == null || $email == null || $username == null) {
    echo '<br>Xác nhận thay đổi email thất bại!';
    exit;
  }
  if ($game->query("SELECT `email` FROM `user` WHERE `email` = '".$game->real_escape_string($email)."';")->num_rows > 0) {
    echo '<br>Email đã được sử dụng!';
    exit;
  }
  if (!preg_match('/^.+@.+$/', $email)) {
    echo '<br>Email không hợp lệ';
    exit;
  }
  $sql = "UPDATE `user` SET `email` = '".$game->real_escape_string($email)."', `token` = '' WHERE `user` = '".$game->real_escape_string($username)."' AND `token` = '".$game->real_escape_string($token)."' AND `active` = 1 AND `email` != '';";
  $game->query($sql);
  if ($game->affected_rows > 0) {
    echo '<br>Xác nhận thay đổi email thành công!';
  } else {
    echo '<br>Xác nhận thay email thất bại!';
  }
} else if ($get['type'] == 'addsignature') {
  $token    = addslashes($get['token']);
  $email    = addslashes($get['email']);
  $password = addslashes($get['password2']);
  if ($token == null || $email == null || $password == null) {
    echo '<br>Xác nhận chữ ký thất bại!';
    exit;
  }
  if (strlen($password) < 6 || strlen($password) > 12) {
    echo '<br>Chữ ký phải từ 6 đến 12 kí tự!';
    exit;
  }
  $sql = "UPDATE `user` SET `password2` = '".$game->real_escape_string($password)."', `token` = '' WHERE `email` = '".$game->real_escape_string($email)."' AND `token` = '".$game->real_escape_string($token)."' AND `active` = 1;";
  $game->query($sql);
  if ($game->affected_rows > 0) {
    echo '<br>Xác nhận chữ ký thành công!';
  } else {
    echo '<br>Xác nhận chữ ký thất bại!';
  }
}
?>