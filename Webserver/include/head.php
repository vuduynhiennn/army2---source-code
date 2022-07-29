<?php 
  include 'config.php';
?>
<!DOCTYPE html>
<html lang="vi-vn">
<head>
  <meta charset="utf-8"/>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <meta name="viewport" content="width=device-width,minimum-scale=1" />
  <meta http-equiv="content-language" content="vi" />
  <title><?php  echo $title; ?></title>
  <meta name="author" content="<?php  echo $config['author']; ?>">
  <meta name="keywords" content="<?php  echo $config['keywords']; ?>" />
  <meta name="description" content="<?php  echo $config['description']; ?>" />
  <meta name="robots" content="INDEX,FOLLOW" />
  <meta property="og:title" content="<?php  echo $title; ?>" />
  <meta property="og:type" content="website" />
  <meta property="og:url" content="<?php  echo $config['site']; ?>" />
  <meta property="og:description" content="<?php  echo $config['description']; ?>" />
  <meta property="og:image" content="<?php  echo $config['background']; ?>" />
  <meta property="og:site_name" content="<?php  echo $config['site_name']; ?>" />
  <meta name="theme-color" content="#ffd77e"/>
  <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon" />
  <link rel="apple-touch-icon" href="/favicon.ico" />
  <link rel="stylesheet" href="<?php  echo $config['css']; ?>">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bttn.css/0.2.4/bttn.min.css">
  <script src="https://cdn.jsdelivr.net/npm/sweetalert2@10"></script>
  <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js" integrity="sha384-q2kxQ16AaE6UbzuKqyBE9/u/KzioAlnx2maXQHiDX9d4/zp8Ok3f+M7DPm+Ib6IU" crossorigin="anonymous"></script>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/js/bootstrap.min.js" integrity="sha384-pQQkAEnwaBkjpqZ8RU1fF1AKtTcHJwFl3pblpTlHXybJjHpMYo79HY3hIi4NKxyj" crossorigin="anonymous"></script>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
  <script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
  <script src="/handler/query.js?t=<? echo time(); ?>"></script>

</head>
<body>
<div class="body_body">
	<div style="line-height: 10px;
    font-size: 10px;
    padding-right: 5px;
    margin-bottom: -10px;
    padding-top: 10px;">
    <img height=12 alt="12 tuổi" src="/public/images/12.png" style="vertical-align: middle;" />
    <span style="vertical-align: middle;">Dành cho người chơi trên 12 tuổi. Chơi quá 180 phút mỗi ngày sẽ hại sức khỏe.
			</span>
</div>
<div class="body-content">
  <div class="bg-content2">
    <h1 class="a" align="center">
      <a href="/" title="Trang Chủ MobiArmy II">
        <img src="/public/images/logo123.png" alt="MobiArmy II" width="50%">
      </a>
    </h1>
    <div id="top">
      <div class="link-more">
        <div class="h" align="center">
          <div class="bg_noel"></div>
            <div class="menu2">
              <table border="0" cellspacing="5">
                <tr class="menu">
                
                <?php 
                  $path = $_SERVER['REQUEST_URI'];
                  if ($path === "/") {
                    echo '<td id="selected" style="padding: 2px; font-family: \'Amatic SC\', cursive; font-size: 11px;"><a href="/" >Trang Chủ</a></td>';
                  } else {
                    echo '<td style="padding: 2px; font-size: 11px;"><a href="/" >Trang Chủ</a></td>';
                  }
                  echo "\n";
                  if ($path === "/about") {
                    echo '<td id="selected" style="padding: 2px; font-family: \'Amatic SC\', cursive; font-size: 11px;"><a href="/about" >Giới Thiệu</a></td>';
                  } else {
                    echo '<td style="padding: 2px; font-size: 11px;"><a href="/about" >Giới Thiệu</a></td>';
                  }
                  if (!isset($_SESSION['user_id'])) {
                    echo "\n";
                    if ($path === "/login") {
                      echo '<td id="selected" style="padding: 2px; font-family: \'Amatic SC\', cursive; font-size: 11px;"><a href="/login" >Đăng Nhập</a></td>';
                    } else {
                      echo '<td style="padding: 2px; font-size: 11px;"><a href="/login" >Đăng Nhập</a></td>';
                    }
                    echo "\n";
                    if ($path === "/register") {
                      echo '<td id="selected" style="padding: 2px; font-family: \'Amatic SC\', cursive; font-size: 11px;"><a href="/register" >Đăng Ký</a></td>';
                    } else {
                      echo '<td style="padding: 2px; font-size: 11px;"><a href="/register" >Đăng Ký</a></td>';
                    }
                  } else {
                    if ($path === "/profile") {
                      echo '<td id="selected" style="padding: 2px; font-family: \'Amatic SC\', cursive; font-size: 11px;"><a href="/profile" >Thông tin</a></td>';
                    } else {
                      echo '<td style="padding: 2px; font-size: 11px;"><a href="/profile" >Thông tin</a></td>';
                    }
                    echo '<td style="padding: 2px; font-size: 11px;"><a href="/team" >Biệt đội</a></td>';
                  }
                  echo "\n";
                  if ($path === "/ranking") {
                    echo '<td id="selected" style="padding: 2px; font-family: \'Amatic SC\', cursive; font-size: 11px;"><a href="/ranking" >Cao Thủ</a></td>';
                  } else {
                    echo '<td style="padding: 2px; font-size: 11px;"><a href="/ranking" >Cao Thủ</a></td>';
                  }
                ?>
                
                </tr>
              </table>
            </div>