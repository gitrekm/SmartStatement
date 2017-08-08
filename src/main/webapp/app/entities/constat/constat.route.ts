import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ConstatComponent } from './constat.component';
import { ConstatDetailComponent } from './constat-detail.component';
import { ConstatPopupComponent } from './constat-dialog.component';
import { ConstatDeletePopupComponent } from './constat-delete-dialog.component';
import { ConstatPredictComponent } from './constat-predict.component';

export const constatRoute: Routes = [
    {
        path: 'constat',
        component: ConstatComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartStatementApp.constat.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'constat/:id',
        component: ConstatDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartStatementApp.constat.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
            path: 'predict/:id',
            component: ConstatPredictComponent,
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartStatementApp.constat.home.title'
            },
            canActivate: [UserRouteAccessService]
        }
];

export const constatPopupRoute: Routes = [
                  {
                      path: 'constat-new',
                      component: ConstatPopupComponent,
                      data: {
                          authorities: ['ROLE_USER'],
                          pageTitle: 'smartStatementApp.constat.home.title'
                      },
                      canActivate: [UserRouteAccessService],
outlet: 'popup'
    },
    {
        path: 'constat/:id/edit',
        component: ConstatPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartStatementApp.constat.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'constat/:id/delete',
        component: ConstatDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartStatementApp.constat.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
