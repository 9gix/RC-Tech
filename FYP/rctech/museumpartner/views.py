# Create your views here.
from django.views.generic.simple import direct_to_template
from .models import Exhibit
from django.shortcuts import get_object_or_404


def index(request):
    e = Exhibit()
    e.save()
    return direct_to_template(request, 'museumpartner/index.html', {})

def add(request):
    return direct_to_template(request,'museumpartner/add.html',{})