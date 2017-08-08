import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Circonstances } from './circonstances.model';
import { CirconstancesPopupService } from './circonstances-popup.service';
import { CirconstancesService } from './circonstances.service';
import { Constat, ConstatService } from '../constat';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-circonstances-dialog',
    templateUrl: './circonstances-dialog.component.html'
})
export class CirconstancesDialogComponent implements OnInit {

    circonstances: Circonstances;
    isSaving: boolean;

    constats: Constat[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private circonstancesService: CirconstancesService,
        private constatService: ConstatService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.constatService.query()
            .subscribe((res: ResponseWrapper) => { this.constats = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.circonstances.id !== undefined) {
            this.subscribeToSaveResponse(
                this.circonstancesService.update(this.circonstances));
        } else {
            this.subscribeToSaveResponse(
                this.circonstancesService.create(this.circonstances));
        }
    }

    private subscribeToSaveResponse(result: Observable<Circonstances>) {
        result.subscribe((res: Circonstances) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Circonstances) {
        this.eventManager.broadcast({ name: 'circonstancesListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackConstatById(index: number, item: Constat) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-circonstances-popup',
    template: ''
})
export class CirconstancesPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private circonstancesPopupService: CirconstancesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.circonstancesPopupService
                    .open(CirconstancesDialogComponent as Component, params['id']);
            } else {
                this.circonstancesPopupService
                    .open(CirconstancesDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
