# Create your views here.
from django.views.generic.simple import direct_to_template
from .models import Exhibit,Video,Audio,Info
from django.shortcuts import get_object_or_404
from django.utils import simplejson as json
from django.http import HttpResponse


def index(request): 
    i = Exhibit.objects.all()
    return direct_to_template(request, 'museumpartner/index.html', {'exhibits':i})

def json_view(request,qr):
    exhibit = get_object_or_404(Exhibit,qrcode=qr)
    audio_objects = Audio.objects.filter(exhibit=exhibit).order_by("order","title")
    video_objects = Video.objects.filter(exhibit=exhibit).order_by("title")
    info_objects = Info.objects.filter(exhibit=exhibit).order_by("type","title")
    
    audio_list = []
    for audio in audio_objects:
        audio_list.append({'title':audio.title,'order':audio.order,'link':audio.link})
    
    info_list = []
    for info in info_objects:
        info_list.append({'type':info.type,'title':info.title,'link':info.link})
    
    video_list = []
    for video in video_objects:
        video_list.append({'title':video.title,'link':video.link})
    return HttpResponse(json.dumps({'name':exhibit.name,'audio':audio_list,'video':video_list,'info':info_list},sort_keys=False), mimetype="application/json")

