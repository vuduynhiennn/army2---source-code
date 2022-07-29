<?php
  include './include/config.php';
  unset($_SESSION['user_id']);
  header('location: /login');
?>