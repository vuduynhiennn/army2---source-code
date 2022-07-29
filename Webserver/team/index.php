<?php
include ('res/connect.php');

if (!$login)
    $_GET['c'] = 'login';
    
switch ($_GET['c'])  {

    case 'login':
        
        include ('login.php');
        
        break;
        
    case 'fr':
        
        include ('fr.php');
        
        break;
        
    case 'create':
        
        include ('create.php');
        
        break;
        
    case 'list':
        
        include ('list.php');
        
        break;
        
    case 'join':
        
        include ('join.php');
        
        break;
        
    case 'outclan':
        
        include ('out.php');
        
        break;
        
    case 'info':
        
        include ('info.php');
        
        break;
        
    case 'remaster':
        
        include ('refreshmaster.php');

        break;
        

    case 'addmem':
        
        include ('addmember.php');
        
        break;
        
    default:
    
    include ('list.php');
    
        break;

}

?>