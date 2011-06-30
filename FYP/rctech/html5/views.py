# Create your views here.
from django.views.generic.simple import direct_to_template
from django.http import HttpResponse

def index(request):
    return direct_to_template(request, 'html5/index.html')