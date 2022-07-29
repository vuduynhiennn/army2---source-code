<?php
  date_default_timezone_set('Asia/Ho_Chi_Minh');
  session_name('kitakeyos');
  session_start();
  $config = array();
  
  // info
  $config['author']       = 'kitakeyos';
  $config['keywords']     = 'army2, army ii, mobiarmy, mobiarmy ii, army lau, mobiarmy lau, kitakeyos';
  $config['description']  = 'Sống lại một thời hào hùng cùng tựa game siêu hấp dẫn.';
  $config['css']          = '/public/css/template.min.css?v='.time();
  $config['background']   = '/public/images/background.png';
  $config['site_name']    = 'Mobi Army II';
  $config['site']          = 'http://army2.ml';

  // mysql
  $config['mysql']['server']       = 'localhost';
  $config['mysql']['port']         = 3306;
  $config['mysql']['user']         = 'root';
  $config['mysql']['password']     = '';
  $config['mysql']['database']     = 'dbarmy2';

  // smtp
  $config['smtp']['server']        = 'smtp.gmail.com';
  $config['smtp']['port']          = 587;
  $config['smtp']['secure']        = 'tls';
  $config['smtp']['username']      = 'account_gmail@gmail.com';
  $config['smtp']['password']      = 'password_gmail';
  $config['smtp']['from']          = 'cloud.serarmy@gmail.com';
  $config['smtp']['from_name']     = 'MobiArmy II';
  $config['smtp']['reply']         = 'kitakeyos@gmail.com';
  $config['smtp']['reply_name']    = 'Hoang Huu Dung';
  $config['smtp']['charset']       = 'UTF-8';
  $config['smtp']['language']      = 'en';
  $config['smtp']['html']          = true;
  $config['smtp']['auth']          = true;
  $config['smtp']['debug']         = false;
  
  // api cardvip
  $config['api']['key'] = '692640f9-efef-42a4-9752-360d7ce53de0';
  $config['api']['url'] = 'http://partner.cardvip.vn/api/createExchange';
?>