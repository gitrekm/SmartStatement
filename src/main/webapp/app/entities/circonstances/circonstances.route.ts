import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { CirconstancesComponent } from './circonstances.component';
import { CirconstancesDetailComponent } from './circonstances-detail.component';
import { CirconstancesPopupComponent } from './circonstances-dialog.component';
import { CirconstancesDeletePopupComponent } from './circonstances-delete-dialog.component';

export const circonstancesRoute: Routes = [
    {
        path: 'circonstances',
        component: CirconstancesComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartStatementApp.circonstances.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'circonstances/:id',
        component: CirconstancesDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartStatementApp.circonstances.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const circonstancesPopupRoute: Routes = [
    {
        path: 'circonstances-new',
        component: CirconstancesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartStatementApp.circonstances.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'circonstances/:id/edit',
        component: CirconstancesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartStatementApp.circonstances.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'circonstances/:id/delete',
        component: CirconstancesDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartStatementApp.circonstances.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
