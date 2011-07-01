# Create your views here.
from django.http import HttpResponse
from django.shortcuts import render, render_to_response
from django.views.generic.simple import direct_to_template

def index(request):
    return direct_to_template(request, 'html5/index.html',{})