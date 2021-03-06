from django.conf.urls.defaults import *

from django.contrib import admin
admin.autodiscover()

handler500 = 'djangotoolbox.errorviews.server_error'

urlpatterns = patterns('',
    (r'^admin/', include(admin.site.urls)),
    (r'^html5/', include('html5.urls')),
    (r'^museum/', include('museumpartner.urls')),
    ('^_ah/warmup$', 'djangoappengine.views.warmup'),
    ('^$', 'django.views.generic.simple.redirect_to',
     {'url': '/museum'}),
)
