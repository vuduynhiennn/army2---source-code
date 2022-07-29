<?php
if (isset($_POST)) {
  include '../include/config.php';
  include '../include/connect.php';
  if (isset($_SESSION['user_id'])) {
    $id = $_POST['id'];
    $password = $_POST['password'];
    $email = $_POST['email'];
    $password2 = $_POST['signature'];
    $msg = array();
    if ($id == null || $id == '' || $password == null || $password == '' || $password2 == null || $password2 == '') {
      $msg['status'] = false;
      $msg['message'] = 'Không được bỏ trống.';
      echo json_encode($msg);
      return;
    }
    if (!preg_match("/^[0-9]+$/", $id)) {
      $msg['status'] = false;
      $msg['message'] = 'Mã tài khoản không hợp lệ.';
      echo json_encode($msg);
      return;
    }
    if (!preg_match("/^.+@.+$/", $email)) {
      $msg['status'] = false;
      $msg['message'] = 'Email không hợp lệ.';
      echo json_encode($msg);
      return;
    }
    if ($game->query("SELECT `user` FROM `user` WHERE `email` = '".$email."' LIMIT 1;")->num_rows == 1) {
      $msg['status'] = false;
      $msg['message'] = 'Email này đã có người dùng.';
      echo json_encode($msg);
      return;
    }
    $sql = "SELECT * FROM `shop` WHERE `id` = ".$id." AND `buy` = 0 LIMIT 1;";
    $r = $game->query($sql);
    if ($r->num_rows == 1) {
      $r = $r->fetch_assoc();
      if ($r['user_id'] == $user['user_id']) {
        $msg['status'] = false;
        $msg['message'] = 'Bạn không thể mua tài khoản bạn đăng bán.';
        echo json_encode($msg);
        return;
      } else if ($armymem['money'] < $r['price']) {
        $msg['status'] = false;
        $msg['message'] = 'Bạn không đủ tiền.';
        echo json_encode($msg);
        return;
      } else {
        $game->query("UPDATE `armymem` SET `money` = `money` - ".$r['price']." WHERE `id` = ".$user['user_id']." LIMIT 1;");
        $game->query("UPDATE `armymem` SET `money` = `money` + ".$r['price']." WHERE `id` = ".$r['user_id']." LIMIT 1;");
        $game->query("UPDATE `shop` SET `buy` = 1 WHERE `id` = ".$r['id']." LIMIT 1;");
        $game->query("UPDATE `user` SET `password` = '".$game->real_escape_string($password)."', `password2` = '".$game->real_escape_string($password2)."', `email` = '".$game->real_escape_string($email)."' WHERE `user_id` = ".$r['account_id']." LIMIT 1;");
        $da = $game->query("SELECT `user` FROM `user` WHERE `user_id` = ".$r['account_id']." LIMIT 1;");
        if ($da->num_rows == 1) {
          $da = $da->fetch_assoc();
          $msg['status'] = true;
          $msg['message'] = "Mua tài khoản thành công!. Tài khoản: ".$da['user'].' - Mật khẩu: '.$password.' - Email: '.$email.' - Chữ ký: '.$password2;
          echo json_encode($msg);
          return;
        } else {
          $msg['status'] = false;
          $msg['message'] = 'Có lỗi xảy ra.';
          echo json_encode($msg);
          return;
        }
      }
    } else {
      $msg['status'] = false;
      $msg['message'] = 'Hệ thống không tìm thấy tài khoản này.';
      echo json_encode($msg);
      return;
    }
  }
}


?>