# Create your views here.
from django.views.generic.simple import direct_to_template
from django.http import HttpResponse
from django.shortcuts import render, render_to_response

def index(request):

    return direct_to_template(request, 'html5/index.html',{})