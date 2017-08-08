import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Assurance } from './assurance.model';
import { AssurancePopupService } from './assurance-popup.service';
import { AssuranceService } from './assurance.service';
import { Constat, ConstatService } from '../constat';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-assurance-dialog',
    templateUrl: './assurance-dialog.component.html'
})
export class AssuranceDialogComponent implements OnInit {

    assurance: Assurance;
    isSaving: boolean;

    constats: Constat[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private assuranceService: AssuranceService,
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
        if (this.assurance.id !== undefined) {
            this.subscribeToSaveResponse(
                this.assuranceService.update(this.assurance));
        } else {
            this.subscribeToSaveResponse(
                this.assuranceService.create(this.assurance));
        }
    }

    private subscribeToSaveResponse(result: Observable<Assurance>) {
        result.subscribe((res: Assurance) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Assurance) {
        this.eventManager.broadcast({ name: 'assuranceListModification', content: 'OK'});
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

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-assurance-popup',
    template: ''
})
export class AssurancePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private assurancePopupService: AssurancePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.assurancePopupService
                    .open(AssuranceDialogComponent as Component, params['id']);
            } else {
                this.assurancePopupService
                    .open(AssuranceDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
