<?php
  function replace_str($str, $len) {
    return str_repeat('*', strlen($str)-$len).
    substr($str, strlen($str)-$len, strlen($str));
  }
?>