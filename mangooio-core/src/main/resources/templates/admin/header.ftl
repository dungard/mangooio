<#setting number_format=",##0">
<#setting locale="en_US">
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>mangoo I/O | Control Panel</title>
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <style><#include "css/bootstrap.min.css"></style>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css">	
    <style><#include "css/AdminLTE.min.css"></style>
    <style><#include "css/skin-blue.min.css"></style>
    <style><#include "css/blue.min.css"></style>
    <style><#include "css/jquery-jvectormap.min.css"></style>
  </head>
  <body class="hold-transition skin-blue sidebar-mini">
    <div class="wrapper">
      <header class="main-header">
        <a href="/" class="logo">
          <span class="logo-mini">I/O</span>
          <span class="logo-lg"><b>mangoo</b> I/O</span>
        </a>
        <nav class="navbar navbar-static-top" role="navigation">
        <a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
            <span class="sr-only">Toggle navigation</span>
          </a>
        </nav>
      </header>
      <aside class="main-sidebar">
        <section class="sidebar">
          <ul class="sidebar-menu">
            <li <#if !space??>class="active"</#if>><a href="/@admin"><i class="fa fa-dashboard"></i><span>Dashboard</span></a></li>
            <li <#if space?? && space == 'configuration'>class="active"</#if>><a href="/@admin/configuration"><i class="fa fa-cog"></i><span>Configuration</span></a></li>
            <li <#if space?? && space == 'routes'>class="active"</#if>><a href="/@admin/routes"><i class="fa fa-arrows"></i><span>Routes</span></a></li>
            <li <#if space?? && space == 'scheduler'>class="active"</#if>><a href="/@admin/scheduler"><i class="fa fa-calendar"></i><span>Scheduler</span></a></li>
            <li <#if space?? && space == 'cache'>class="active"</#if>><a href="/@admin/cache"><i class="fa fa-cube"></i><span>Cache</span></a></li>
            <li <#if space?? && space == 'metrics'>class="active"</#if>><a href="/@admin/metrics"><i class="fa fa-signal"></i><span>Metrics</span></a></li>
          </ul>
        </section>
      </aside>
      <div class="content-wrapper">
        <section class="content">