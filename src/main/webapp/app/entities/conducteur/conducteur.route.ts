import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ConducteurComponent } from './conducteur.component';
import { ConducteurDetailComponent } from './conducteur-detail.component';
import { ConducteurPopupComponent } from './conducteur-dialog.component';
import { ConducteurDeletePopupComponent } from './conducteur-delete-dialog.component';

export const conducteurRoute: Routes = [
    {
        path: 'conducteur',
        component: ConducteurComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartStatementApp.conducteur.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'conducteur/:id',
        component: ConducteurDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartStatementApp.conducteur.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const conducteurPopupRoute: Routes = [
    {
        path: 'conducteur-new',
        component: ConducteurPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartStatementApp.conducteur.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'conducteur/:id/edit',
        component: ConducteurPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartStatementApp.conducteur.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'conducteur/:id/delete',
        component: ConducteurDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartStatementApp.conducteur.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
