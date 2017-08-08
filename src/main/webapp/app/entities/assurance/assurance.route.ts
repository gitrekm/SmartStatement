import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { AssuranceComponent } from './assurance.component';
import { AssuranceDetailComponent } from './assurance-detail.component';
import { AssurancePopupComponent } from './assurance-dialog.component';
import { AssuranceDeletePopupComponent } from './assurance-delete-dialog.component';

export const assuranceRoute: Routes = [
    {
        path: 'assurance',
        component: AssuranceComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartStatementApp.assurance.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'assurance/:id',
        component: AssuranceDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartStatementApp.assurance.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const assurancePopupRoute: Routes = [
    {
        path: 'assurance-new',
        component: AssurancePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartStatementApp.assurance.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'assurance/:id/edit',
        component: AssurancePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartStatementApp.assurance.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'assurance/:id/delete',
        component: AssuranceDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartStatementApp.assurance.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
