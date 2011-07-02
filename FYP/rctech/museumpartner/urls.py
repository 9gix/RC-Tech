from django.conf.urls.defaults import *

urlpatterns = patterns('museumpartner.views',
    (r'^$','index'),
    (r'^json_view/(\w+)/$','json_view'),
)
