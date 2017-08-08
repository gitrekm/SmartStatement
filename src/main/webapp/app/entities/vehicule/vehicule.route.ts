import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { VehiculeComponent } from './vehicule.component';
import { VehiculeDetailComponent } from './vehicule-detail.component';
import { VehiculePopupComponent } from './vehicule-dialog.component';
import { VehiculeDeletePopupComponent } from './vehicule-delete-dialog.component';

export const vehiculeRoute: Routes = [
    {
        path: 'vehicule',
        component: VehiculeComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartStatementApp.vehicule.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'vehicule/:id',
        component: VehiculeDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartStatementApp.vehicule.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const vehiculePopupRoute: Routes = [
    {
        path: 'vehicule-new',
        component: VehiculePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartStatementApp.vehicule.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'vehicule/:id/edit',
        component: VehiculePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartStatementApp.vehicule.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'vehicule/:id/delete',
        component: VehiculeDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartStatementApp.vehicule.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
